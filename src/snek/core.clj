(ns snek.core)

(def board-height 32)
(def board-width 32)

(def space 0)
(def wall  \#)
(def munchy  \X)
(def head  \+)
(def tail  \+)

(defn board-new[height width]
  (->> space
       (repeat width)
       vec
       (repeat height)
       vec))

(defn snake-new [[y x] direction]
  {:position [y x] :direction direction})

(defn snake-move-position [{:keys [position direction] :as snake}]
  (let [[y x] position]
    (condp = direction
      :north (snake-new [(dec y) x] direction)
      :south (snake-new [(inc y) x] direction)
      :west  (snake-new [y (dec x)] direction)
      :east  (snake-new [y (inc x)] direction)
      :stopped snake)))

(def game-new
  {:board (board-new board-height board-width)
   :snake (snake-new [10 10] :east)
   :food [20,20]})

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

(defn random-direction[{:keys [direction]}]
  (first
   (shuffle
    (remove (partial = direction) [:north :south, :west, :east]))))

(defn snake-turn[snake]
  (Thread/sleep 100)
  (update snake :direction random-direction))

(defn board-move
  "Snakes head becomes first part of the tail"
  [board snake-head]
  (mapv (fn [row] (mapv #(if (and (int? %) (pos? %)) (dec %) %) row))
          (update-in board snake-head (constantly 3)
                     )))

(defn crashed?[board {:keys [position]}]
  (let [cell (get-in board position)]
    (or (nil? cell)
        (pos? cell))))

(let [board [[0 0 0]
             [0 0 0]
             [0 0 0]]]
  (crashed? board (snake-new [-1 -1] :stopped)))

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
                      :snake (snake-move-position (snake-turn snake))
                      :food food
                      :board (board-move board (:position snake))
                      })))))

(defn -main []
  (start-game))
