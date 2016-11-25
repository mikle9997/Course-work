package com.maltsev.labyrinth.model.field;

import java.util.ArrayList;

/**
 *  Игровое поле
 */
public class GameField {

    /**
     *  Матрица ячеек, т.е. само поле
     */
    private ArrayList< ArrayList< CellOfField>> field;

    /**
     *  Массив координат ячеек, которые являются проходимыми
     */
    private ArrayList<PointOnTheField> passableCells;

    /**
     *  Размер поля по оси X
     */
    private int sizeOfFieldX;

    /**
     *  Размер поля по оси Y
     */
    private int sizeOfFieldY;

    /**
     * Стартовая точка поля
     */
    private PointOnTheField startingPoint;

    /**
     * Финишная точка поля
     */
    private PointOnTheField finishingPoint;


    /**
     * Конструктор, в которм создаётся игровое поле
     * @param newField строка-матрица, где 1,s,f - проходимые элементы, а 0 - нет, s - начальная точка поля, f - конечная, а новая строчка задаётся \n
     */
    public GameField(String newField) {

        passableCells = new ArrayList<PointOnTheField>();
        field = new ArrayList<ArrayList<CellOfField>>();

        String[] field = newField.split("\\n");   // делим полученую строчку на части

        int lengthX = field.length;
        int lengthY = field[field.length - 1].length();


        for(int x = 0; x < lengthX; x++) {                  // Проверка на то, чтобы матрица была прямоугольной,
                                                           // если это не так, то шириной берётся минимальное значение
            if (lengthY != field[x].length()) {           // остальное отбрасывается

                if (lengthY > field[x].length()) {

                    lengthY = field[x].length();
                }
            }
        }

        boolean metBeginning = false;
        boolean metEnd = false;

        for(int x = 0; x < lengthX; x++) {

            ArrayList< CellOfField> arrayOfCell = new ArrayList<CellOfField>();

            for(int y = 0; y < lengthY; y++) {

                boolean isItPossibleWay = false;
                if (field[x].charAt(y) != '0') {            // Установка проходимости

                    isItPossibleWay = true;

                    passableCells.add(new PointOnTheField(x,y));
                }

                arrayOfCell.add(new CellOfField(isItPossibleWay));

                if (field[x].charAt(y) == 's' || field[x].charAt(y) == 'S') {        // Установка стартовой и финишной точек

                    if (!metBeginning) startingPoint = new PointOnTheField(x,y);
                    metBeginning = true;
                }
                else if (field[x].charAt(y) == 'f' || field[x].charAt(y) == 'F') {

                    if (!metEnd) finishingPoint = new PointOnTheField(x,y);
                    metEnd = true;
                }
            }
            this.field.add(arrayOfCell);
        }

        setTheValuesOfSizeOfField();

        if (!metBeginning) {

            PointOnTheField startPoint = passableCells.get(0);
            this.startingPoint = new PointOnTheField(startPoint);
        }
        if (!metEnd) {

            PointOnTheField finishPoint = passableCells.get(passableCells.size() - 1);
            this.finishingPoint = new PointOnTheField(finishPoint);
        }
    }     //TODO хотелось бы уменьшить объём метода, но не знаю как

    /**
     *  Фиксирование размеров поля
     */
    private void setTheValuesOfSizeOfField() {

        sizeOfFieldX = field.size();
        sizeOfFieldY = field.get(0).size();
    }

    /**
     * @param x - координата точки по оси X
     * @param y - координата точки по оси Y
     * @return является ли эта ячейка проходимой
     * @throws OutOfBoundaryOfTheField - воход за граниуц поля
     */
    public boolean isItPassableCell(int x, int y) throws OutOfBoundaryOfTheField {

        if (x < 0 || x >= sizeOfFieldX) throw new OutOfBoundaryOfTheField("Illegal request of possible way", "x", x, sizeOfFieldX - 1);
        if (y < 0 || y >= sizeOfFieldY) throw new OutOfBoundaryOfTheField("Illegal request of possible way", "y", y, sizeOfFieldY - 1);

        return field.get(x).get(y).getInfoAboutPatencyOfCell();
    }

    /**
     * @param point точка на поле указывающая на ячейку
     * @return является ли эта ячейка проходимой
     * @throws OutOfBoundaryOfTheField - воход за граниуц поля
     */
    public boolean isItPassableCell(PointOnTheField point) throws OutOfBoundaryOfTheField {

        int x = point.getX();
        int y = point.getY();

        if (x < 0 || x >= sizeOfFieldX) throw new OutOfBoundaryOfTheField("Illegal request of possible way", "x", x, sizeOfFieldX - 1);
        if (y < 0 || y >= sizeOfFieldY) throw new OutOfBoundaryOfTheField("Illegal request of possible way", "y", y, sizeOfFieldY - 1);

        return field.get(x).get(y).getInfoAboutPatencyOfCell();
    }

    /**
     * @return размер поля по оси X
     */
    public int getSizeOfFieldX() {

        return sizeOfFieldX;
    }

    /**
     * @return размер поля по оси Y
     */
    public int getSizeOfFieldY() {

        return sizeOfFieldY;
    }

    /**
     * @return начальная точка поля
     */
    public PointOnTheField getStartingPoint() {

        return startingPoint;
    }

    /**
     * @return конечная точка поля
     */
    public PointOnTheField getFinishingPoint() {

        return finishingPoint;
    }

    /**
     * @return Массив координат проходимых ячеек
     */
    public ArrayList<PointOnTheField> getPassableCells() {

        return passableCells;
    }
}