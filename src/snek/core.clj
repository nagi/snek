(ns snek.core)

(def board-height 16)
(def board-width 16)

(def space 0)
(def apple  \X)
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
  (print (str (char 27) "[2J")) ; clear screen
  (print (str (char 27) "[;H")) ; move cursor to top left
  (let [tiles (for [row (range board-height)]
                (for [cell (range board-width)]
                  (cond
                    (= [row cell] position) head
                    (= [row cell] food) apple
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
  ((comp first shuffle) (all-blanks board)))

(defn replace-food[{:keys [position] :as snake} food board]
  (if (= position food)
    (empty-position board)
    food))

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
                      :board (board-move board snake)
                      })))))

(defn -main []
  (start-game))
