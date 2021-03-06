package fr.mgargadennec.blossom.core.group;

import org.hibernate.validator.constraints.NotBlank;

public class GroupUpdateForm {

  public GroupUpdateForm() {
  }

  public GroupUpdateForm(GroupDTO group) {
    this.name = group.getName();
    this.description = group.getDescription();
  }

  @NotBlank(message = "{groups.group.validation.name.NotBlank.message}")
  private String name = "";

  @NotBlank(message = "{groups.group.validation.description.NotBlank.message}")
  private String description = "";

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

}
