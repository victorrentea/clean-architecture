package victor.training.clean;

import com.structurizr.Workspace;
import com.structurizr.export.plantuml.C4PlantUMLExporter;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.ComponentView;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.tngtech.archunit.core.importer.ImportOption.Predefined.DO_NOT_INCLUDE_TESTS;

// Regenerates adoc/architecture.dsl, adoc/views/packages.puml, adoc/views/packages.svg on every run.
// Commit all three to git — they stay in sync with the code automatically.
// Runs from clean-application/ module dir (standard Maven / IntelliJ Maven runner behavior).
public class PackageDiagramTest {

    private static final String BASE = "victor.training.clean";

    @Test
    void generatePackageDiagram() throws Exception {
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(DO_NOT_INCLUDE_TESTS)
                .importPackages(BASE);

        Map<String, String> packageDefs = new LinkedHashMap<>();
        packageDefs.put("domain",      BASE + ".domain");
        packageDefs.put("application", BASE + ".application");
        packageDefs.put("infra",       BASE + ".infra");
        packageDefs.put("vsa",         BASE + ".vsa");

        Workspace workspace = new Workspace("Clean Architecture",
                "Auto-extracted from bytecode — do not edit manually");
        SoftwareSystem system = workspace.getModel().addSoftwareSystem("Clean Application");
        Container app = system.addContainer("clean-application", "Spring Boot app", "Java 17");

        Map<String, Component> components = new LinkedHashMap<>();
        for (var entry : packageDefs.entrySet()) {
            boolean hasClasses = classes.stream()
                    .anyMatch(c -> c.getPackageName().startsWith(entry.getValue()));
            if (hasClasses) {
                components.put(entry.getKey(),
                        app.addComponent(entry.getKey(), entry.getValue()));
            }
        }

        List<String[]> edges = new ArrayList<>();
        for (var from : packageDefs.entrySet()) {
            Component fromComp = components.get(from.getKey());
            if (fromComp == null) continue;
            for (var to : packageDefs.entrySet()) {
                if (from.getKey().equals(to.getKey())) continue;
                Component toComp = components.get(to.getKey());
                if (toComp == null) continue;

                boolean depends = classes.stream()
                        .filter(c -> c.getPackageName().startsWith(from.getValue()))
                        .flatMap(c -> c.getDirectDependenciesFromSelf().stream())
                        .anyMatch(dep -> dep.getTargetClass().getPackageName()
                                .startsWith(to.getValue()));
                if (depends) {
                    fromComp.uses(toComp, "");
                    edges.add(new String[]{from.getKey(), to.getKey()});
                }
            }
        }

        ComponentView view = workspace.getViews()
                .createComponentView(app, "packages", "Top-level package dependencies");
        view.addAllComponents();
        view.enableAutomaticLayout();

        Files.createDirectories(Path.of("../adoc/views"));

        // 1) Structurizr DSL — open at structurizr.com or render with the CLI
        write("../adoc/architecture.dsl", buildDsl(components, edges));

        // 2) C4 PlantUML — generated from the same model, matches the DSL view
        String puml = new C4PlantUMLExporter().export(workspace).stream()
                .filter(d -> d.getKey().equals("packages"))
                .findFirst().orElseThrow()
                .getDefinition();
        write("../adoc/views/packages.puml", puml);

        // 3) SVG — pre-rendered so it's viewable on GitHub without tooling
        ByteArrayOutputStream svgBytes = new ByteArrayOutputStream();
        new SourceStringReader(puml).outputImage(svgBytes, new FileFormatOption(FileFormat.SVG));
        write("../adoc/views/packages.svg", svgBytes.toString(StandardCharsets.UTF_8));
    }

    private static String buildDsl(Map<String, Component> components, List<String[]> edges) {
        var s = new StringBuilder();
        s.append("workspace \"Clean Architecture\" \"Auto-extracted from bytecode — do not edit manually\" {\n\n");
        s.append("    model {\n\n");
        s.append("        cleanApp = softwareSystem \"Clean Application\" {\n\n");
        s.append("            app = container \"clean-application\" \"Spring Boot app\" \"Java 17\" {\n\n");
        components.forEach((name, comp) ->
                s.append("                ").append(name)
                 .append(" = component \"").append(name).append("\" \"").append(comp.getDescription()).append("\"\n"));
        s.append("\n");
        edges.forEach(e ->
                s.append("                ").append(e[0]).append(" -> ").append(e[1]).append(" \"\"\n"));
        s.append("\n            }\n        }\n    }\n\n");
        s.append("    views {\n\n");
        s.append("        component app \"packages\" \"Top-level package dependencies\" {\n");
        s.append("            include *\n");
        s.append("            autoLayout lr\n");
        s.append("        }\n\n");
        s.append("        styles {\n");
        s.append("            element \"Component\" { background #85bbf0 color #ffffff }\n");
        s.append("        }\n\n");
        s.append("    }\n\n}\n");
        return s.toString();
    }

    private static void write(String path, String content) throws IOException {
        Path p = Path.of(path);
        Files.writeString(p, content);
        System.out.println("Written: " + p.toAbsolutePath().normalize());
    }
}
