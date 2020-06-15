(ns snek.graphics
  (:require [quil.core :as q]
            [quil.middleware :as m]))

;; (def apple  \X)
;; (def head  \+)
;; (def tail  \.)
;; (def blank \space)

;; (defn paint[{:keys [board snake food], {:keys [position direction]} :snake}]
;;   (print (str (char 27) "[2J")) ; clear screen
;;   (print (str (char 27) "[;H")) ; move cursor to top left
;;   (let [board-height (count board)
;;         board-width (count (first board))
;;         tiles (for [row (range board-height)]
;;                 (for [cell (range board-width)]
;;                   (cond
;;                     (= [row cell] position) head
;;                     (= [row cell] food) apple
;;                     (pos? ((board row) cell)) tail
;;                     :else blank
;;                     )))
;;         picture (clojure.string/join "\n"
;;                              (map #(apply str %1) tiles))]
;;     (println picture)))

(def cell-size 32)
(def game-state (atom {}))

(defn paint[game]
  (println "old state: " game)
  (swap! game-state (identity game)))

(defn setup []
  (q/frame-rate 4)
  (q/background 200))

(defn draw []
  (q/stroke (q/random 255))             ;; Set the stroke colour to a random grey
  (q/stroke-weight (q/random 10))       ;; Set the stroke thickness randomly
  (q/fill (q/random 255))               ;; Set the fill colour to a random grey

  (let [x 20
        y 0]
    (q/ellipse x y cell-size cell-size)))


(defn init-gfx[board-height board-width]
  (q/defsketch example                  ;; Define a new sketch named example
    :title "Oh so many grey circles"    ;; Set the title of the sketch
    :settings #(q/smooth 2)             ;; Turn on anti-aliasing
    :setup setup                        ;; Specify the setup fn
    :draw draw                          ;; Specify the draw fn
    :size [(* board-width cell-size) (* board-height cell-size)]))

