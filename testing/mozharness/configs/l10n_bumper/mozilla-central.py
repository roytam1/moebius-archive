MULTI_REPO = "mozilla-central"
config = {
    "log_name": "l10n_bumper",

    "exes": {
        # Get around the https warnings
        "hg": ["/usr/local/bin/hg", "--config", "web.cacerts=/etc/pki/tls/certs/ca-bundle.crt"],
        "hgtool.py": ["/usr/local/bin/hgtool.py"],
    },

    "goanna_pull_url": "https://hg.mozilla.org/{}".format(MULTI_REPO),
    "goanna_push_url": "ssh://hg.mozilla.org/{}".format(MULTI_REPO),

    "hg_user": "L10n Bumper Bot <release+l10nbumper@mozilla.com>",
    "ssh_key": "~/.ssh/ffxbld_rsa",
    "ssh_user": "ffxbld",

    "vcs_share_base": "/builds/hg-shared",
    "version_path": "browser/config/version.txt",

    "bump_configs": [{
        "path": "mobile/locales/l10n-changesets.json",
        "format": "json",
        "name": "Fennec l10n changesets",
        "platform_configs": [{
            "platforms": ["android-api-15", "android"],
            "path": "mobile/android/locales/all-locales"
        }, {
            "platforms": ["android-multilocale"],
            "path": "mobile/android/locales/maemo-locales"
        }],
    }],
}
