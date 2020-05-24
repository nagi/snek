(ns snek.controls)

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

(defn direct-snake[& args]
  (apply human-player args))
