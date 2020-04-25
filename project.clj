(defproject trellotaskbot "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [environ             "1.1.0"]
                 [morse               "0.2.4"]
                 [com.draines/postal "2.0.3"]]

  :plugins [[lein-environ "1.1.0"]]

  :main ^:skip-aot trellotaskbot.core
  :target-path "target/%s"

  :profiles {:dev {:env {:telegram-token ""
                         :trello-mail ""
                         :mail-server "smtp.yandex.ru"
                         :mail-port "465"
                         :mail-user ""
                         :mail-pass ""
                         :allowed-user ""}}
             :uberjar {:aot :all}})
