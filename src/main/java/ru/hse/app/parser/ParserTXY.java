package ru.hse.app.parser;

import ru.hse.app.domain.Point;

public class ParserTXY implements IParser {

    @Override
    public Point parse(String line) throws NumberFormatException {
        String[] numbersStr = line.split(" ");
        if(numbersStr.length != 3) {
            throw new NumberFormatException("Invalid input file format: " + line);
        }
        double t = Double.parseDouble(numbersStr[0]);
        double x = Double.parseDouble(numbersStr[1]);
        double y = Double.parseDouble(numbersStr[2]);
        return new Point(t, x, y);
    }

}
