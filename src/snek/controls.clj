(ns snek.controls)

(defn random-direction[{:keys [direction]}]
  (first
   (shuffle
    (remove (partial = direction) [:north :south, :west, :east]))))

;; Map based implementation is clearer
(defn rotate-a-snake[direction rotation]
  (let[clockwise [:north, :east, :south, :west, :north]
       anti-clockwise (reverse clockwise)]
    (condp = rotation
      :clockwise (second (drop-while (partial not= direction) clockwise))
      :anti-clockwise (second (drop-while (partial not= direction) anti-clockwise)))))

(defn human-player-rotate-a-snake[direction]
  (let [ui (read-line)]
    (condp = ui
      "j" (rotate-a-snake direction :anti-clockwise)
      "l" (rotate-a-snake direction :clockwise)
      direction)))

(defn human-player-vim-keys[direction]
  (let [ui (read-line)]
    (condp = ui
      "j" :south
      "k" :north
      "h" :west
      "l" :east)))

(defn direct-snake[old-direction]
  (human-player-rotate-a-snake old-direction))
