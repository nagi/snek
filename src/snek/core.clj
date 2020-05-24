(ns snek.core
  (:require
   [snek.graphics :refer [paint]]
   [snek.controls :refer [direct-snake]]
   ))

(def board-height 16)
(def board-width 16)
(def blank-cell 0)

(defn board-new[height width]
  (->> blank-cell
       (repeat width)
       vec
       (repeat height)
       vec))

(defn snake-new [[y x] direction length]
  {:position [y x] :direction direction :length length})

(defn snake-move-position [{:keys [position direction length] :as snake}]
  (let [[y x] position
        move-position (fn [new-position] (assoc snake :position new-position))]
    (condp = direction
      :north (move-position [(dec y) x])
      :south (move-position [(inc y) x])
      :west  (move-position [y (dec x)])
      :east  (move-position [y (inc x)])
      :stopped snake)))

(def game-new
  {:board (board-new board-height board-width)
   :snake (snake-new [5 5] :east 3)
   :food [10,10]})

(defn snake-turn[snake]
  (Thread/sleep 100)
  (update snake :direction
          direct-snake))

(defn board-move
  "Snakes head becomes first part of the tail"
  [board {:keys [position length]}]
  (mapv (fn [row] (mapv #(if (and (int? %) (pos? %)) (dec %) %) row))
          (update-in board position (constantly length))))

(defn crashed?[board {:keys [position]}]
  (let [cell (get-in board position)]
    (or (nil? cell) ;; Out of board
        (pos? cell)))) ;; Crashed into tail

(defn eat-and-grow[food {:keys [position] :as snake}]
  (if (= position food)
    (update snake :length inc)
    snake))

(defn all-positions[board]
  (for [y (range (count board))
        x (range (count (first board)))]
    [y x]))

(defn all-blanks[board]
  (filter
   (fn [coords]
     ((every-pred int? zero?)
      (get-in board coords)))
   (all-positions board)))

(defn empty-position[board]
  (println board)
  ((comp first shuffle) (all-blanks board)))

(defn replace-food[{:keys [position] :as snake} food board]
  (if (= position food)
    (empty-position (assoc-in board position "CHOMP!"))))

(defn start-game[]
  (loop [turns 99999
         {:keys [board snake food] :as game} game-new]
    (cond
      (zero? turns) (println "Out of turns")
      (crashed? board snake) (println "Crashed - Game Over")
      :else (do
              (paint game)
              (recur (dec turns)
                     {
                      :snake (->> snake
                                  (eat-and-grow food)
                                  snake-turn
                                  snake-move-position)
                      :food (replace-food snake food board)
                      :board (board-move board snake)})))))

(defn -main []
  (start-game))
