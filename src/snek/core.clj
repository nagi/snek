(ns snek.core)

(def board-height 16)
(def board-width 16)

(def space 0)
(def munchy  \X)
(def head  \+)

(defn board-new[height width]
  (->> space
       (repeat width)
       vec
       (repeat height)
       vec))

(defn snake-new [[y x] direction length]
  {:position [y x] :direction direction :length length})

(defn snake-move-position [{:keys [position direction length] :as snake}]
  (let [[y x] position]
    (condp = direction
      :north (snake-new [(dec y) x] direction length)
      :south (snake-new [(inc y) x] direction length)
      :west  (snake-new [y (dec x)] direction length)
      :east  (snake-new [y (inc x)] direction length)
      :stopped snake)))

(def game-new
  {:board (board-new board-height board-width)
   :snake (snake-new [5 5] :east 3)
   :food [10,10]})

(defn paint[{:keys [board snake food], {:keys [position direction]} :snake}]
  (let [tiles (for [row (range board-height)]
                (for [cell (range board-width)]
                  (cond
                    (= [row cell] position) head
                    (= [row cell] food) munchy
                    :else ((board row) cell))))
        picture (clojure.string/join "\n"
                             (map #(apply str %1) tiles))]
    (println picture)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Control Strats - replace with A.I.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn random-direction[{:keys [direction]}]
  (first
   (shuffle
    (remove (partial = direction) [:north :south, :west, :east]))))

(defn human-player[{:keys [direction]}]
  (let [ui (read-line)]
    (println direction)
    (condp = ui
      "j" :south
      "k" :north
      "h" :west
      "l" :east)))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn snake-turn[snake]
  (Thread/sleep 100)
  (update snake :direction
          human-player
          ;; random-direction
          ))

(defn board-move
  "Snakes head becomes first part of the tail"
  [board {:keys [position length]}]
  (mapv (fn [row] (mapv #(if (and (int? %) (pos? %)) (dec %) %) row))
          (update-in board position (constantly length))))

(defn crashed?[board {:keys [position]}]
  (let [cell (get-in board position)]
    (or (nil? cell)
        (pos? cell))))

(defn eat-and-grow[{:keys [position] :as snake} food]
  (if (= position food)
    (update snake :length inc)
    snake))

(defn replace-food[board]
  [8 8])

(defn start-game[]
  (loop [turns 30
         {:keys [board snake food] :as game} game-new]
    (cond
      (zero? turns) (println "Out of turns")
      (crashed? board snake) (println "Crashed - Game Over")
      :else (do
              (print (str (char 27) "[2J")) ; clear screen
              (print (str (char 27) "[;H")) ; move cursor to top left
              (paint game)
              (recur (dec turns)
                     {
                      :snake (snake-move-position (snake-turn (eat-and-grow snake food)))
                      :food (replace-food board)
                      :board (board-move board snake)
                      })))))

(defn -main []
  (start-game))
