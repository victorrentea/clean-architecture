//package victor.training.clean.persistence;
//
//@Entity
//@Getter
//@Setter
////@Data // Lombok avoid on ORM @Entity because:
//// 1) hashCode uses @Id⚠️
//// 2) toString might trigger lazy-loading⚠️
//// 3) all setters = no encapsulation⚠️
//public class CustomerEntity {
//  @Id
//  @GeneratedValue
//  private Long id;
//  private String name;
//  private String email;
//
//  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
//  private String shippingAddressCity; // remove
//  private String shippingAddressStreet; // remove
//  private String shippingAddressZip; // remove
//
//  @Embedded
//  private ShippingAddress shippingAddress;
//
//  @ManyToOne
//  private Country country;
//
////  @JsonFormat(pattern = "yyyy-MM-dd")//
//  private LocalDate createdDate;
//  private String createdByUsername;
//
//  private boolean goldMember;
////  @JsonIgnore
//  private String goldMemberRemovalReason;
//
////  private String bannedByWhoPhonenumber;
//
//  private String legalEntityCode;
//  private boolean discountedVat;
//
//  public int getDiscountPercentage() {
//    int discountPercentage = 1;
//    if (goldMember) {
//      discountPercentage += 3;
//    }
//    return discountPercentage;
//  }
//
//
//  public enum Status {
//    DRAFT, VALIDATED, ACTIVE, DELETED
//  }
//  @Setter(AccessLevel.NONE)
//  private Status status = Status.DRAFT;
//  @Setter(AccessLevel.NONE)
//  private String validatedBy; // ⚠ this field is not-null when status = VALIDATED or later
//
////  public void setStatus(Status status, String who)
//  public void validate(String byWho) {
//    if (status != Status.DRAFT) {
//      throw new IllegalStateException();
//    }
//    status = Status.VALIDATED;
//    validatedBy = byWho;
//  }
//  //
//}