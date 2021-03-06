package fr.mgargadennec.blossom.autoconfigure.ui.web.administration;

import fr.mgargadennec.blossom.autoconfigure.core.CommonAutoConfiguration;
import fr.mgargadennec.blossom.core.association_user_group.AssociationUserGroupService;
import fr.mgargadennec.blossom.core.common.utils.privilege.Privilege;
import fr.mgargadennec.blossom.core.common.utils.privilege.SimplePrivilege;
import fr.mgargadennec.blossom.core.group.GroupService;
import fr.mgargadennec.blossom.core.user.UserService;
import fr.mgargadennec.blossom.ui.web.administration.membership.MembershipsController;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(MembershipsController.class)
@AutoConfigureAfter(CommonAutoConfiguration.class)
public class WebAdministrationMembershipAutoConfiguration {

    @Bean
    public MembershipsController membershipsController(AssociationUserGroupService associationUserGroupService, UserService userService, GroupService groupService) {
        return new MembershipsController(associationUserGroupService, userService, groupService);
    }

    @Bean
    public Privilege membershipsReadPrivilegePlugin() {
        return new SimplePrivilege("administration", "memberships", "read");
    }

    @Bean
    public Privilege membershipsChangePrivilegePlugin() {
        return new SimplePrivilege("administration", "memberships", "change");
    }

}
