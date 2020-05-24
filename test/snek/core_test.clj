(ns snek.core-test
  (:require [clojure.test :refer :all]
            [snek.core :refer :all]))

(deftest snake-movment
  (testing "move a snake north"
    (is
     (= (:position (snake-move-position (snake-new [5 5] :north 3))) [4 5])))
  (testing "move a snake south"
    (is
     (= (:position (snake-move-position (snake-new [5 5] :south 3))) [6 5])))
  (testing "move a static west"
    (is
     (= (:position (snake-move-position (snake-new [5 5] :west 3))) [5 4])))
  (testing "move a snake east"
    (is
     (= (:position (snake-move-position (snake-new [5 5] :east 3))) [5 6])))
  (testing "move a stopped snake"
    (is
     (= (:position (snake-move-position (snake-new [5 5] :stopped 3))) [5 5]))))

(deftest snake-crashed
  (let [board [[0 0]
               [0 1]]]
    (testing "inside board"
      (is
       (false? (crashed? board (snake-new [0 0] :stopped 3))))
      (is
       (false? (crashed? board (snake-new [0 1] :stopped 3))))
      (is
       (false? (crashed? board (snake-new [1 0] :stopped 3)))))
    (testing "outside board"
      (is
       (true? (crashed? board (snake-new [-1 0] :stopped 3))))
      (is
       (true? (crashed? board (snake-new [0 -1] :stopped 3))))
      (is
       (true? (crashed? board (snake-new [1 2] :stopped 3))))
      (is
       (true? (crashed? board (snake-new [2 1] :stopped 3)))))
    (testing "crashed into tail"
      (is
       (true? (crashed? board (snake-new [1 1] :stopped 3)))))))

(deftest replacing-blanks
  (let [board [[0 0 0]
               [0 1 "CHOMP!"]]]
    (testing "finds all blanks"
      (is
       (= 
          #{
          [0 0] [0 1] [0 2]
          [1 0] ;;;;;;;;;;;
          }
        (set (all-blanks board)))))))

(deftest replacing-food
  (let [board [[2 1 0]
               [3 4 0]]]
    (testing "finds the blank"
      (is
       (= [0 2]
          (replace-food {:position [2 1]} [2 1] board))))))
