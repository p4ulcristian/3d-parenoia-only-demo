// WARNING: DO NOT EDIT!
// THIS FILE WAS GENERATED BY SHADOW-CLJS AND WILL BE OVERWRITTEN!

var ALL = {};
ALL["@three-ts/orbit-controls"] = require("@three-ts/orbit-controls");
ALL["three"] = require("three");
ALL["react-dom/client"] = require("react-dom/client");
ALL["highlight.js/lib/languages/clojure"] = require("highlight.js/lib/languages/clojure");
ALL["react-dom"] = require("react-dom");
ALL["highlight.js/lib/core"] = require("highlight.js/lib/core");
ALL["react"] = require("react");
global.shadow$bridge = function shadow$bridge(name) {
  var ret = ALL[name];

  if (ret === undefined) {
     throw new Error("Dependency: " + name + " not provided by external JS. Do you maybe need a recompile?");
  }

  return ret;
};

shadow$bridge.ALL = ALL;