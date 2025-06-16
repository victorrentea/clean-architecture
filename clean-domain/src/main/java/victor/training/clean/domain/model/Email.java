package victor.training.clean.domain.model;

import java.util.ArrayList;
import java.util.List;

public final class Email {
  private final String to;
  private final List<String> cc = new ArrayList<>();
  private final String from;
  private final String subject;
  private final String body;

  @java.beans.ConstructorProperties({"to", "from", "subject", "body"})
  Email(String to, String from, String subject, String body) {
    this.to = to;
    this.from = from;
    this.subject = subject;
    this.body = body;
  }

  public static EmailBuilder builder() {
    return new EmailBuilder();
  }

  public String getTo() {
    return this.to;
  }

  public List<String> getCc() {
    return this.cc;
  }

  public String getFrom() {
    return this.from;
  }

  public String getSubject() {
    return this.subject;
  }

  public String getBody() {
    return this.body;
  }

  public boolean equals(final Object o) {
    if (o == this) return true;
    if (!(o instanceof Email)) return false;
    final Email other = (Email) o;
    final Object this$to = this.getTo();
    final Object other$to = other.getTo();
    if (this$to == null ? other$to != null : !this$to.equals(other$to)) return false;
    final Object this$cc = this.getCc();
    final Object other$cc = other.getCc();
    if (this$cc == null ? other$cc != null : !this$cc.equals(other$cc)) return false;
    final Object this$from = this.getFrom();
    final Object other$from = other.getFrom();
    if (this$from == null ? other$from != null : !this$from.equals(other$from)) return false;
    final Object this$subject = this.getSubject();
    final Object other$subject = other.getSubject();
    if (this$subject == null ? other$subject != null : !this$subject.equals(other$subject)) return false;
    final Object this$body = this.getBody();
    final Object other$body = other.getBody();
    if (this$body == null ? other$body != null : !this$body.equals(other$body)) return false;
    return true;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final Object $to = this.getTo();
    result = result * PRIME + ($to == null ? 43 : $to.hashCode());
    final Object $cc = this.getCc();
    result = result * PRIME + ($cc == null ? 43 : $cc.hashCode());
    final Object $from = this.getFrom();
    result = result * PRIME + ($from == null ? 43 : $from.hashCode());
    final Object $subject = this.getSubject();
    result = result * PRIME + ($subject == null ? 43 : $subject.hashCode());
    final Object $body = this.getBody();
    result = result * PRIME + ($body == null ? 43 : $body.hashCode());
    return result;
  }

  public String toString() {
    return "Email(to=" + this.getTo() + ", cc=" + this.getCc() + ", from=" + this.getFrom() + ", subject=" + this.getSubject() + ", body=" + this.getBody() + ")";
  }

  public static class EmailBuilder {
    private String to;
    private String from;
    private String subject;
    private String body;

    EmailBuilder() {
    }

    public EmailBuilder to(String to) {
      this.to = to;
      return this;
    }

    public EmailBuilder from(String from) {
      this.from = from;
      return this;
    }

    public EmailBuilder subject(String subject) {
      this.subject = subject;
      return this;
    }

    public EmailBuilder body(String body) {
      this.body = body;
      return this;
    }

    public Email build() {
      return new Email(this.to, this.from, this.subject, this.body);
    }

    public String toString() {
      return "Email.EmailBuilder(to=" + this.to + ", from=" + this.from + ", subject=" + this.subject + ", body=" + this.body + ")";
    }
  }
}
