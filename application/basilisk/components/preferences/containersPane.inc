# This Source Code Form is subject to the terms of the Mozilla Public
# License, v. 2.0. If a copy of the MPL was not distributed with this
# file, You can obtain one at http://mozilla.org/MPL/2.0/.

<!-- Containers panel -->

<script type="application/javascript"
        src="chrome://browser/content/preferences/containersPane.js"/>

<preferences id="containerPreferences" hidden="true" data-category="paneContainer">
  <!-- Containers -->
  <preference id="privacy.userContext.enabled"
              name="privacy.userContext.enabled"
              type="bool"/>

</preferences>

<hbox hidden="true"
      class="container-header-links"
      data-category="paneContainers">
  <label class="text-link" id="backContainersLink" value="&backLink.label;" />
</hbox>

<hbox id="header-containers"
      class="header"
      hidden="true"
      data-category="paneContainers">
  <label class="header-name" flex="1">&paneContainers.title;</label>
  <button class="help-button"
          aria-label="&helpButton.label;"/>
</hbox>

<!-- Containers -->
<groupbox id="browserContainersGroup" data-category="paneContainers" hidden="true">
  <vbox id="browserContainersbox">

    <richlistbox id="containersView" orient="vertical" persist="lastSelectedType"
                 flex="1">
      <listheader equalsize="always">
          <treecol id="typeColumn" value="type"
                   persist="sortDirection"
                   flex="1" sortDirection="ascending"/>
          <treecol id="actionColumn" value="action"
                   persist="sortDirection"
                   flex="1"/>
      </listheader>
    </richlistbox>
  </vbox>
  <vbox>
    <hbox flex="1">
      <button onclick="gContainersPane.onAddButtonClick();" accesskey="&addButton.accesskey;" label="&addButton.label;"/>
    </hbox>
  </vbox>
</groupbox>
