(ns trellotaskbot.core
  (:require [clojure.core.async :refer [<!!]]
            [clojure.string :as ss]
            [environ.core :refer [env]]
            [postal.core :as postal]
            [morse.handlers :as h]
            [morse.polling :as p]
            [morse.api :as t])
  (:gen-class))

(def token (env :telegram-token))

(h/defhandler handler
  (h/command-fn "start"
    (fn [{{id :id :as chat} :chat}]
      (println "Bot joined new chat: " chat)
      (t/send-text token id "Welcome to trellotaskbot! It currently works for just one user")))

  (h/command-fn "help"
    (fn [{{id :id :as chat} :chat}]
      (println "Help was requested in " chat)
      (t/send-text token id "Help is on the way")))

  (h/message-fn
   (fn [{{id :id} :chat text :text}]
     (let [[subj & details] (ss/split-lines text)
           details (if details
                     (ss/join "\n" details)
                     "")]
       (if (= (str id) (env :allowed-user))
         (do
           (postal/send-message {:host (env :mail-server)
                                 :port (Integer/parseInt (env :mail-port))
                                 :user (env :mail-user)
                                 :sender (env :mail-user)
                                 :pass (env :mail-pass)
                                 :ssl true}
                                {:from (env :mail-user)
                                 :to (env :trello-mail)
                                 :subject subj
                                 :body details})
           (t/send-text token id "Recorded!"))
         (t/send-text token id "I don't know you"))))))

(defn -main
  [& args]
  (when (ss/blank? token)
    (println "Please provide token in TELEGRAM_TOKEN environment variable!")
    (System/exit 1))

  (println "Starting the trellotaskbot")
  (<!! (p/start token handler)))

;; (def wow (p/start token handler))
;; (clojure.core.async/close! wow)