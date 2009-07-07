(ns build
  (:import [org.apache.tools.ant.taskdefs Manifest])
  (:use lancet))

(def src "src")
(def vendor "vendor")
(def test-dir "test")
(def conjure-src (str vendor "/conjure"))
(def target "target")
(def classes (str target "/classes"))
(def test-app (str target "/test_app"))
(def file-structure "file_structure")
(def default (str file-structure "/default"))

(deftarget compile-conjure "Compile Conjure sources."
  (mkdir { :dir classes })
  (javac { :srcdir src
           :destdir classes
           :debug "on" }))
           
(deftarget jar-conjure "Creates the conjure.jar file."
  (compile-conjure)
  (jar { :jarfile (str target "/conjure.jar")
         :manifest "MANIFEST.MF" }
    (fileset { :dir classes })
    (fileset { :dir file-structure })))

(deftarget clean "Remove autogenerated files and directories."
  (delete { :dir target }))
  
(deftarget test-conjure "Run all of the tests for Conjure."
  (jar-conjure)
  (java { :jar (str target "/conjure.jar")
          :fork "true"
          :dir target }
    [:arg { :value "test_app" }])
  (copy { :todir (str test-app "/test") }
    (fileset { :dir test-dir }))
  (echo { :message "\nRunning Tests...\n\n"})
  (java { :classname "clojure.lang.Script"
          :dir test-app
          :fork "true" }
    [:arg { :value "test/run_tests.clj" }]
    [:classpath  
      [:pathElement { :path (str test-app "/vendor") }]
      [:pathElement { :path (str test-app "/app") }]
      [:pathElement { :path (str test-app "/config") }]
      [:pathElement { :path (str test-app "/script") }]
      [:pathElement { :path (str test-app "/db") }]
      [:pathElement { :path (str test-app "/test") }]
      (fileset { :dir (str test-app "/lib")
                 :includes "**/*.jar" })]))

(deftarget all "Builds all of conjure."
  (jar-conjure)
  (test-conjure))
  
(deftarget default "Executing default target."
  (all))

(if (not-empty *command-line-args*)
  (apply -main *command-line-args*)
  (default))