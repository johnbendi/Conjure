(defproject org.conjure/conjure-server "0.9.0-SNAPSHOT"
  :description "Conjure view is a library to render html for conjure."
  :dependencies [[clojure-tools "1.1.1"]
                 [org.clojure/tools.cli "0.2.1"]
                 [org.clojure/tools.logging "0.2.0"]
                 [org.conjure/conjure-config "0.9.0-SNAPSHOT"]
                 [org.conjure/conjure-plugin "0.9.0-SNAPSHOT"]
                 [org.conjure/conjure-util "0.9.0-SNAPSHOT"]
                 [org.drift-db/drift-db "1.0.7"]
                 [ring/ring-devel "1.1.0"]
                 [ring/ring-jetty-adapter "1.1.0"]
                 [ring/ring-servlet "1.1.0"]]
  
  :profiles { :dev { :dependencies [[log4j/log4j "1.2.16"]
                                    [org.conjure/conjure-flow "0.9.0-SNAPSHOT"]
                                    [org.conjure/conjure-model "0.9.0-SNAPSHOT"]
                                    [org.conjure/conjure-view "0.9.0-SNAPSHOT"]
                                    [org.drift-db/drift-db-h2 "1.0.7"]] } }

  :aot [conjure.server.servlet])