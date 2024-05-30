import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/email-config">
        Email Config
      </MenuItem>
      <MenuItem icon="asterisk" to="/subscription-details">
        Subscription Details
      </MenuItem>
      <MenuItem icon="asterisk" to="/category">
        Category
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
