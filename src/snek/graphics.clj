(ns snek.graphics)

(def apple  \X)
(def head  \+)
(def tail  \.)
(def blank \space)

(defn paint[{:keys [board snake food], {:keys [position direction]} :snake}]
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
