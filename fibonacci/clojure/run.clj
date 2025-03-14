(ns run
  (:require [languages.benchmark :as benchmark])
  (:gen-class))

(set! *unchecked-math* :warn-on-boxed)

(definterface IFib
  (^long fib [^long n]))

(deftype Fibonacci []
  IFib
  (fib [_  n]
    (if (< n 2)
      (long n)
      (long (+ (.fib _ (- n 1))
               (.fib _ (- n 2)))))))

(def ^:private ^Fibonacci fibonacci (Fibonacci.))

(defn -main [& args]
  (let [run-ms (parse-long (first args))
        warmup-ms (parse-long (second args))
        n (parse-long (nth args 2))
        _warmup (benchmark/run #(.fib fibonacci n) warmup-ms)]
    (-> (benchmark/run #(.fib fibonacci n) run-ms)
        benchmark/format-results 
        println)))

(comment
  (-main "10000" "36")
  :rcf)

