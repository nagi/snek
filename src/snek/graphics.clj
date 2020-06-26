(ns snek.graphics
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(def apple  \X)
(def head  \+)
(def tail  \.)
(def blank \space)

(defn console-paint[{:keys [board snake food], {:keys [position direction]} :snake}]
  (print (str (char 27) "[2J")) ; clear screen
  (print (str (char 27) "[;H")) ; move cursor to top left
  (let [board-height (count board)
        board-width (count (first board))
        tiles (for [row (range board-height)]
                (for [cell (range board-width)]
                  (cond
                    (= [row cell] position) head
                    (= [row cell] food) apple
                    (pos? ((board row) cell)) tail
                    :else blank
                    )))
        picture (clojure.string/join "\n"
                             (map #(apply str %1) tiles))]
    (println picture)))

(def cell-size 32)
(def game-state (atom {}))

(defn paint[game]
  (console-paint game)
  (println "old state: " game)
  (reset! game-state (identity game)))

(defn setup []
  (q/smooth)
  (q/frame-rate 10)
  (q/background 200))

(defn draw []
  (let [{:keys [board snake food], {:keys [position direction]} :snake} @game-state
        w (q/screen-width)
        h (q/screen-height)
        board-height (count board)
        board-width (count (first board))]

    (q/fill 55)
    (q/stroke 200)
    (q/stroke-weight 3)

    (q/fill 155)
    (doseq [row (range board-height)
            cell (range board-width)]
      (let[fill-color (cond (= [row cell] position) (q/fill 30 30 160)   ;; head
                            (= [row cell] food) (q/fill 200 30 30)       ;; food
                            (pos? ((board row) cell)) (q/fill 30 30 220) ;; tail
                            :else (q/fill 155))]                         ;; blank
        (do
          (q/rect (* cell cell-size) (* row cell-size) cell-size cell-size))))))

(defn init-gfx[board-height board-width]
  (q/defsketch example                  ;; Define a new sketch named example
    :title "Oh so many grey circles"    ;; Set the title of the sketch
    :settings #(q/smooth 2)             ;; Turn on anti-aliasing
    :setup setup                        ;; Specify the setup fn
    :draw draw                          ;; Specify the draw fn
    :size [(* board-width cell-size) (* board-height cell-size)]))
