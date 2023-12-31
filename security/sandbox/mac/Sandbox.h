/* -*- Mode: C++; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

#ifndef mozilla_Sandbox_h
#define mozilla_Sandbox_h

#include <string>

enum MacSandboxType {
  MacSandboxType_Default = 0,
  MacSandboxType_Plugin,
  MacSandboxType_Content,
  MacSandboxType_Invalid
};

enum MacSandboxPluginType {
  MacSandboxPluginType_Default = 0,
  MacSandboxPluginType_GMPlugin_Default,      // Any Goanna Media Plugin
  MacSandboxPluginType_GMPlugin_OpenH264,     // Goanna Media Plugin, OpenH264
  MacSandboxPluginType_GMPlugin_EME,          // Goanna Media Plugin, EME
  MacSandboxPluginType_GMPlugin_EME_Widevine, // Goanna Media Plugin, Widevine
  MacSandboxPluginType_Invalid
};

typedef struct _MacSandboxPluginInfo {
  _MacSandboxPluginInfo()
    : type(MacSandboxPluginType_Default) {}
  _MacSandboxPluginInfo(const struct _MacSandboxPluginInfo& other)
    : type(other.type), pluginPath(other.pluginPath),
      pluginBinaryPath(other.pluginBinaryPath) {}
  MacSandboxPluginType type;
  std::string pluginPath;
  std::string pluginBinaryPath;
} MacSandboxPluginInfo;

typedef struct _MacSandboxInfo {
  _MacSandboxInfo()
    : type(MacSandboxType_Default), level(0), shouldLog(true) {}
  _MacSandboxInfo(const struct _MacSandboxInfo& other)
    : type(other.type), level(other.level),
      hasSandboxedProfile(other.hasSandboxedProfile),
      pluginInfo(other.pluginInfo),
      appPath(other.appPath), appBinaryPath(other.appBinaryPath),
      appDir(other.appDir), appTempDir(other.appTempDir),
      profileDir(other.profileDir), shouldLog(other.shouldLog) {}
  MacSandboxType type;
  int32_t level;
  bool hasSandboxedProfile;
  MacSandboxPluginInfo pluginInfo;
  std::string appPath;
  std::string appBinaryPath;
  std::string appDir;
  std::string appTempDir;
  std::string profileDir;
  bool shouldLog;
} MacSandboxInfo;

namespace mozilla {

bool StartMacSandbox(MacSandboxInfo aInfo, std::string &aErrorMessage);

} // namespace mozilla

#endif // mozilla_Sandbox_h
