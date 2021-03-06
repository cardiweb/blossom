package fr.mgargadennec.blossom.autoconfigure.ui.web.administration;

import fr.mgargadennec.blossom.core.common.utils.privilege.Privilege;
import fr.mgargadennec.blossom.core.common.utils.privilege.SimplePrivilege;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.autoconfigure.core.CommonAutoConfiguration;
import fr.mgargadennec.blossom.core.common.search.SearchEngineImpl;
import fr.mgargadennec.blossom.core.role.RoleDTO;
import fr.mgargadennec.blossom.core.role.RoleService;
import fr.mgargadennec.blossom.ui.menu.MenuItem;
import fr.mgargadennec.blossom.ui.menu.MenuItemBuilder;
import fr.mgargadennec.blossom.ui.web.administration.role.RolesController;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
@ConditionalOnClass(RolesController.class)
@AutoConfigureAfter(CommonAutoConfiguration.class)
public class WebAdministrationRoleAutoConfiguration {

  @Bean
  public MenuItem administrationRoleMenuItem(MenuItemBuilder builder,
      @Qualifier("administrationMenuItem") MenuItem administrationMenuItem) {
    return builder
      .key("roles")
      .label("menu.administration.roles", true)
      .link("/blossom/administration/roles")
      .icon("fa fa-key")
      .privilege(rolesReadPrivilegePlugin())
      .order(3)
      .parent(administrationMenuItem).build();
  }

  @Bean
  public RolesController rolesController(RoleService roleService, SearchEngineImpl<RoleDTO> searchEngine) {
    return new RolesController(roleService, searchEngine);
  }

  @Bean
  public Privilege rolesReadPrivilegePlugin() {
    return new SimplePrivilege("administration","roles", "read");
  }

  @Bean
  public Privilege rolesWritePrivilegePlugin() {
    return new SimplePrivilege("administration","roles", "write");
  }

  @Bean
  public Privilege rolesCreatePrivilegePlugin() {
    return new SimplePrivilege("administration","roles", "create");
  }

  @Bean
  public Privilege rolesDeletePrivilegePlugin() {
    return new SimplePrivilege("administration","roles", "delete");
  }

}
