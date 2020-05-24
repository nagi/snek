(ns snek.core-test
  (:require [clojure.test :refer :all]
            [snek.core :refer :all]))

(deftest snake-movment
  (testing "move a snake north"
    (is
     (= (:position (snake-move-position (snake-new [5 5] :north))) [4 5])))
  (testing "move a snake south"
    (is
     (= (:position (snake-move-position (snake-new [5 5] :south))) [6 5])))
  (testing "move a static west"
    (is
     (= (:position (snake-move-position (snake-new [5 5] :west))) [5 4])))
  (testing "move a snake east"
    (is
     (= (:position (snake-move-position (snake-new [5 5] :east))) [5 6])))
  (testing "move a stopped snake"
    (is
     (= (:position (snake-move-position (snake-new [5 5] :stopped))) [5 5]))))

(deftest snake-crashed
  (let [board [[0 0]
               [0 1]]]
    (testing "inside board"
      (is
       (false? (crashed? board (snake-new [0 0] :stopped))))
      (is
       (false? (crashed? board (snake-new [0 1] :stopped))))
      (is
       (false? (crashed? board (snake-new [1 0] :stopped)))))
    (testing "outside board"
      (is
       (true? (crashed? board (snake-new [-1 0] :stopped))))
      (is
       (true? (crashed? board (snake-new [0 -1] :stopped))))
      (is
       (true? (crashed? board (snake-new [1 2] :stopped))))
      (is
       (true? (crashed? board (snake-new [2 1] :stopped)))))
    (testing "crashed into tail"
      (is
       (true? (crashed? board (snake-new [1 1] :stopped)))))))

