(ns snek.core)

(def board-height 5)
(def board-width 5)

(def space \.)
(def wall  \#)
(def munchy  \o)
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

(defn snake-move [{:keys [position direction] :as snake}]
  (let [[y x] position]
    (condp = direction
      :north (snake-new [(dec y) x] direction)
      :south (snake-new [(inc y) x] direction)
      :west  (snake-new [y (dec x)] direction)
      :east  (snake-new [y (inc x)] direction)
      :stopped snake)))

(def game-new
  {:board (board-new board-height board-width)
   :snake (snake-new [1 0] :east)
   :food [2,2]})

(defn paint[{:keys [board snake food], {:keys [position direction]} :snake} ]
  (let [tiles (for [row (range board-height)]
                (for [cell (range board-width)]
                  (cond
                    (= [row cell] position) head
                    (= [row cell] food) munchy
                    :else ((board row) cell))))
        picture (clojure.string/join "\n"
                             (map #(apply str %1) tiles))]
    (println picture)))

(defn start-game[]
  (loop [turns 5
         {:keys [board snake food] :as game} game-new]
    (if (zero? turns)
      (println "Game Over")
      (do
        (paint game)
        (println "next...")
        (recur (dec turns)
               {
                :snake (snake-move snake)
                :food food
                :board board
                })))))

(defn -main []
  (start-game))
