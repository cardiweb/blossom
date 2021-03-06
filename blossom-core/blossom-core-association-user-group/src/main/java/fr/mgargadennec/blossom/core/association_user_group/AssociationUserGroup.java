package fr.mgargadennec.blossom.core.association_user_group;

import fr.mgargadennec.blossom.core.common.entity.AbstractAssociationEntity;
import fr.mgargadennec.blossom.core.group.Group;
import fr.mgargadennec.blossom.core.user.User;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "blossom_association_user_group")
public class AssociationUserGroup extends AbstractAssociationEntity<User, Group> {

  @ManyToOne(fetch = FetchType.EAGER)
  @Fetch(FetchMode.JOIN)
  @JoinColumn(name = "user_id",referencedColumnName = "id")
  private User a;

  @ManyToOne(fetch = FetchType.EAGER)
  @Fetch(FetchMode.JOIN)
  @JoinColumn(name = "group_id",referencedColumnName = "id")
  private Group b;

  public User getUser() {
    return a;
  }

  public void setUser(User user) {
    this.a = user;
  }

  public Group getGroup() {
    return b;
  }

  public void setGroup(Group group) {
    this.b = group;
  }

  @Override
  public User getA() {
    return this.a;
  }

  @Override
  public void setA(User user) {
    this.a = user;
  }

  @Override
  public Group getB() {
    return this.b;
  }

  @Override
  public void setB(Group group) {
    this.b = group;
  }
}
