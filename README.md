# Forge

A client-server tool that syncs configurations, dotfiles and setups between your machines. Expandable, flexible and fast, written in Scala.

## How to build

Forge is built with [gradle]() simply by typing

```console
$ gradle build
```

Done. There are pre-packaged versions for Arch Linux in the [AUR]().

## How to use it

Forge takes care of the task of synchronising config files, special setups as well as custom scripts across all of your machines. It starts with creating a `client.conf` that tells forge where to look for updates. This configuration is written in json and looks a little like this:

```json
{
  "hostname": "idenna",
  "server": {
    "url": "forge.spacekookie.de",
    "port": 443,
    "auth_token": "d275a731a03885298bba0c7b10ec20ac155aa3b5eae66dac806a082d73612b4d"
  },
  "git_key": "none",
  "two_way": false
}
```

After registering a client with a server it will be notified when there are new configurations available to download. The configurations are copied to the desired places according to the `forge.conf` which is placed with the configuration files (preferably in a [git](https://git-scm.com/) repository.

On the server side you can generate new client auth_tokens with the following utility.

```console
$ ~> forgectl gen --user spacekookie --client idenna
d275a731a03885298bba0c7b10ec20ac155aa3b5eae66dac806a082d73612b4d
```

This token will be stored in the server database. You can remove tokens again with `forgectl del <token>`

**The way that forge knows that there have been made changes to your configurations is via a git-hook on whatever hosting provider you use**. What this means is that you need to register a hook for `POST <forge url>:<forge port>/update`. So for the default configuration that would be `POST https://forge.spacekookie.de/update`. You can provide a security token which you can generate with.

```console
$ ~ > forgectl gen --hook
aed2e5e91389e3636ae3bb7b29b5c6e1983f78a58b11f1ca3c1f83541712ce79
```

This way all of your clients will be notified when you update your git repisitory

## The `forge.conf`

The way that forge knows what files go where (and you can easily check yourself) you use the forge config which should be located with your configuration and dot files. This is an example one

```json
{
  "fileset": [
    { "id": "ssh", "path": "~/.ssh/", "type": "folder" },
    { "id": "fish", "path": "~/.config/fish/", "type": "folder" },
    { "id": "tmux", "path": "~/.tmux.conf", "type": "file" },
    { "id": "awesome", "path": "~/.config/awesome/", "type": "folder" },
    { "id": "scripts", "path": "~/.local/bin/", "type": "folder", "flags": ["executable"] }
  ],
  "exclude": {
    "idenna": [ "scripts" ]
  },
  "exclusive": {
    "idenna": [ "awesome" ]
  }
  "force_access": "0700",
  "force_user": "spacekookie",
  "force_group": "geeks"
}
```

You can see that you have a lot of control over how your sort your files and you also have a central place to edit your quirky setups without the need of local alias hacks, symlinks and similar. This is where `forge` shines!

## Contributions

The software is currently still in `pre-alpha` and may not function properly. Thus please be careful when using it. Patches are always welcome! <3