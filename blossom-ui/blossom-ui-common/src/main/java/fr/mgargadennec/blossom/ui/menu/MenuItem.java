package fr.mgargadennec.blossom.ui.menu;

import fr.mgargadennec.blossom.ui.current_user.CurrentUser;
import java.util.Collection;

/**
 * Created by Maël Gargadennnec on 05/05/2017.
 */
public interface MenuItem extends MenuItemPlugin {

  String key();

  String icon();

  String label();

  boolean i18n();

  String link();

  int level();

  int order();

  MenuItem parent();

  String privilege();

  boolean leaf();

  Collection<MenuItem> items();

  Collection<MenuItem> filteredItems(CurrentUser currentUser);

}
