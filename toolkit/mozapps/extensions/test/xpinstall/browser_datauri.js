// ----------------------------------------------------------------------------
// Checks that a chained redirect through a data URI and javascript is blocked

function setup_redirect(aSettings) {
  var url = TESTROOT + "redirect.sjs?mode=setup";
  for (var name in aSettings) {
    url += "&" + name + "=" + encodeURIComponent(aSettings[name]);
  }

  var req = new XMLHttpRequest();
  req.open("GET", url, false);
  req.send(null);
}

function test() {
  waitForExplicitFinish();
  SpecialPowers.pushPrefEnv({
    "set": [["security.data_uri.block_toplevel_data_uri_navigations", false]]
  }, runTest);
}

function runTest() {
  Harness.installOriginBlockedCallback = install_blocked;
  Harness.installsCompletedCallback = finish_test;
  Harness.setup();

  setup_redirect({
    "Location": "data:text/html,<script>window.location.href='" + TESTROOT + "amosigned.xpi'</script>"
  });

  gBrowser.selectedTab = gBrowser.addTab();
  gBrowser.loadURI(TESTROOT + "redirect.sjs?mode=redirect");
}

function install_blocked(installInfo) {
}

function finish_test(count) {
  is(count, 0, "No add-ons should have been installed");
  Services.perms.remove(makeURI("http://example.com"), "install");

  gBrowser.removeCurrentTab();
  Harness.finish();
  finish();
}
