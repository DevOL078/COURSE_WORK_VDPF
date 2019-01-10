package ru.hse.app.parser;

import ru.hse.app.domain.Point;

public interface IParser {

    Point parse(String line) throws Exception;

}
