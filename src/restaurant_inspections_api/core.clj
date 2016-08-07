(ns restaurant-inspections-api.core
  (:require [restaurant-inspections-api.environment :as env]
            [restaurant-inspections-api.routes :refer [all-routes]]
            [restaurant-inspections-api.tasks :refer [load-api-data]]
            [org.httpkit.server :refer [run-server]]
            [ring.middleware.reload :as reload]
            [compojure.handler :refer [site]])
  (:gen-class))

(def port (env/get-env-port))

(defn -main
  "Starts the server"
  [& args]
  (let [handler (if (not (env/in-prod?))
                  (do (println "Server in dev. mode, running hot-reload")
                      (reload/wrap-reload (site #'all-routes)))
                  (do (println "Server in production mode")
                      (site all-routes)))]
    (println "Running server on port " port)
    (load-api-data)
    (run-server handler {:port port})))
