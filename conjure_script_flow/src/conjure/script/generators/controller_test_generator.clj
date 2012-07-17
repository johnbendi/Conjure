(ns conjure.script.generators.controller-test-generator
  (:require [clojure.string :as str-utils]
            [conjure.flow.util :as util]
            [conjure.test.builder :as test-builder]
            [conjure.test.util :as test-util]
            [clojure.tools.file-utils :as file-utils]))

(defn
#^{:doc "Prints out how to use the generate test controller command."}
  controller-usage []
  (println "You must supply a controller name (Like hello-world).")
  (println "Usage: ./run.sh script/generate.clj controller-test <controller> [action]*"))

(defn
  generate-action-test-function [action]
  (str "(deftest test-" action "
  (is (controller-util/call-controller { :controller controller-name :action \"" action "\" })))"))

(defn
#^{:doc "Generates the action functions block for a functional test file."}
  generate-all-action-tests [actions]
  (str-utils/join "\n\n" (map generate-action-test-function actions)))

(defn generate-test-content
  [controller actions]
  (let [test-namespace (test-util/functional-test-namespace controller)
        controller-namespace (util/controller-namespace controller)]
    (str "(ns " test-namespace "
      (:use clojure.test
            " controller-namespace ")
      (:require [conjure.core.controller.util :as controller-util]))
    
    (def controller-name \"" controller "\")
    
    " (generate-all-action-tests actions))))

(defn
#^{:doc "Generates the functional test file for the given controller and actions."}
  generate-functional-test 
  ([controller actions] (generate-functional-test controller actions false))
  ([controller actions silent]
    (when-let [functional-test-file (test-builder/create-functional-test controller silent)]
      (file-utils/write-file-content functional-test-file (generate-test-content controller actions)))))

(defn 
#^{:doc "Generates a controller file for the controller name and actions in params."}
  generate [params]
  (generate-functional-test (first params) (rest params)))