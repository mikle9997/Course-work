package com.maltsev.labyrinth.model.analyzer.event.keysanddoors.doors;


import com.maltsev.labyrinth.model.IModel;
import com.maltsev.labyrinth.model.analyzer.event.EventAnalyzer;

import java.util.*;

/**
 * Извещает интересующихся об открытие двери
 */
public class OpenDoorAnalyzer extends EventAnalyzer{

    private Queue<OpenDoorListener> queue;

    List<com.maltsev.labyrinth.model.field.PointOnTheField> doors;


    public OpenDoorAnalyzer(IModel IModel) {

        super(IModel);

        queue = new LinkedList<OpenDoorListener>();

        doors = IModel.getDoors();
    }

    /**
     * Добавить слушателя
     * @param listener объект-слушатель
     */
    public void addListener(OpenDoorListener listener) {

        queue.add(listener);
    }

    /**
     * Используется, чтобы отписаться от рассылки
     * @param listener - кого удалить
     */
    public void removeListener(OpenDoorListener listener) {

        if (queue.contains(listener))
            queue.remove(listener);
    }

    /**
     * Вызывается при открытие двери, чтобы оповестить об этом слушателей
     */
    public void doorIsOpen(com.maltsev.labyrinth.model.field.PointOnTheField doorPosition) {

        alertListener(doorPosition);
    }

    /**
     * Оповещание слушателей об окончание игры
     */
    private void alertListener(com.maltsev.labyrinth.model.field.PointOnTheField doorPosition) {

        OpenDoorListener item;

        for (int i = 0; i < queue.size(); i++) {

            item = queue.poll();
            item.doorIsOpen(doorPosition);
            queue.add(item);
        }
    }
}
