package com.maltsev.labyrinth.model.analyzer.event.gameover;

import com.maltsev.labyrinth.model.IModel;
import com.maltsev.labyrinth.model.analyzer.event.EventAnalyzer;

import java.util.LinkedList;
import java.util.Queue;

public class GameOverAnalyzer extends EventAnalyzer {

    private com.maltsev.labyrinth.model.field.PointOnTheField finishPoint;

    private Queue<GameOverListener> queue;



    public GameOverAnalyzer(IModel IModel) {

        super(IModel);

        queue = new LinkedList<GameOverListener>();

        finishPoint = new com.maltsev.labyrinth.model.field.PointOnTheField(IModel.getFinishPosition());
    }

    /**
     * Добавить слушателя
     * @param listener объект-слушатель
     */
    public void addListener(GameOverListener listener) {

        queue.add(listener);
    }

    /**
     * Используется, чтобы отписаться от рассылки
     * @param listener - объект-слушатель
     */
    public void removeListener(GameOverListener listener) {

        if (queue.contains(listener))
            queue.remove(listener);
    }


    /**
     * Используется, когда состояние системы изменилось
     */
    public void messageAboutChangingSystem() {

        if (finishPoint.equals(IModel.getPositionOfProtagonist())) {

            alertListener();
        }
    }

    /**
     * Оповещание слушателей об окончание игры
     */
    private void alertListener() {

        GameOverListener item;

        while (!queue.isEmpty()) {

            item = queue.poll();
            item.gameIsOver();
        }
    }
}
