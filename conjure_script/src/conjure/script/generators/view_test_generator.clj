(ns conjure.script.generators.view-test-generator
  (:require [conjure.core.test.builder :as test-builder]
            [conjure.core.test.util :as test-util]
            [clojure.tools.file-utils :as file-utils]
            [conjure.core.view.util :as util]))

(defn
#^{:doc "Prints out how to use the generate test view command."}
  usage []
  (println "You must supply a controller and view name (Like hello-world show).")
  (println "Usage: ./run.sh script/generate.clj view-test <controller> <action>"))

(defn
#^{:doc "Generates the view unit test file for the given controller and action."}
  generate-unit-test 
  ([controller action] (generate-unit-test controller action false))
  ([controller action silent] (generate-unit-test controller action silent nil))
  ([controller action silent incoming-content]
  (let [unit-test-file (test-builder/create-view-unit-test controller action silent)]
    (if unit-test-file
      (let [test-namespace (test-util/view-unit-test-namespace controller action)
            view-namespace (util/view-namespace-by-action controller action)
            test-content (or incoming-content (str "(ns " test-namespace "
  (:use clojure.test
        " view-namespace ")
  (:require [conjure.core.server.request :as request]))

(def controller-name \"" controller "\")
(def view-name \"" action "\")
(def request-map { :controller controller-name
                   :action view-name } )

(deftest test-view
  (request/set-request-map request-map
    (is (render-view))))"))]
        (file-utils/write-file-content unit-test-file test-content))))))

(defn 
#^{:doc "Generates a controller file for the controller name and actions in params."}
  generate [params]
  (generate-unit-test (first params) (second params)))