package subway.dto;

import subway.dao.LineEntity;
import subway.domain.Line;

public class LineResponse {
    private Long id;
    private String name;
    private String color;

    public LineResponse() {
    }

    public LineResponse(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static LineResponse of(Line line) {
        return new LineResponse(line.getId(), line.getNameValue(), line.getColorValue());
    }

    public static LineResponse of(final LineEntity lineEntity) {
        return new LineResponse(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
