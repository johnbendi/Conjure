(ns helpers.template-helper
  (:require [clj-record.core :as clj-record-core]
            [conjure.model.util :as model-util]
            [conjure.util.string-utils :as string-utils]
            [views.layouts.templates.tabs :as layout-tabs]))

(defn
#^{ :doc "Returns the metadata for the given model." }
  table-metadata [model-name]
  (model-util/load-model model-name)
  (clj-record-core/find-by-sql model-name [(str "SHOW COLUMNS FROM " (clj-record-core/table-name model-name))]))

(defn
#^{ :doc "Returns the result of a find records call on the given model with the given attributes" }
  find-records [model-name attributes]
  (model-util/load-model model-name)
  (clj-record-core/find-records model-name attributes))

(defn
#^{ :doc "Returns all of the records of the model with the given name." }
  all-records [model-name]
  (find-records model-name [true]))

(defn
#^{ :doc "Returns the record from the given model with the given id." }
  get-record [model-name id]
  (model-util/load-model model-name)
  (clj-record-core/get-record model-name id))

(defn
  template-tabs [request-map]
  (map 
    (fn [tab-map]
      (let [tab-controller (string-utils/str-keyword (:controller (:url-for tab-map)))]
        (if (and tab-controller (= tab-controller (:controller request-map)))
          (assoc tab-map :is-active true)
          tab-map))) 
    (layout-tabs/all-tabs request-map)))

(defn
#^{ :doc "Returns the record from the given model with the given id." }
  template-request-map 
  ([request-map] (template-request-map request-map (:action request-map)))
  ([request-map action]
    (let [controller (:controller request-map)
          tabs (template-tabs request-map)]
      (merge request-map 
        { :controller "templates", 
          :action action,
          :layout-info { :tabs (template-tabs request-map) } }))))