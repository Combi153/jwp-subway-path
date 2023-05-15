package subway.domain;

import java.util.List;
import java.util.Objects;

public class Line {
    private final Long id;
    private final Name name;
    private final Color color;
    private final Sections sections;

    public Line(final Long id, final String name, final String color, final List<Section> sections) {
        this.id = id;
        this.name = new Name(name);
        this.color = new Color(color);
        this.sections = new Sections(sections);
    }

    public Line(final Long id, final String name, final String color, final Sections sections) {
        this.id = id;
        this.name = new Name(name);
        this.color = new Color(color);
        this.sections = sections;
    }

    public Line(final Long id, final Name name, final Color color, final Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public Line(final String name, final String color, final Sections sections) {
        this(null, name, color, sections);
    }

    public Line insert(final Station from, final Station to, final int distance) {
        return new Line(id, name, color, sections.insert(from, to, distance));
    }

    public Line delete(final Station station) {
        return new Line(id, name, color, sections.delete(station));
    }

    public boolean hasSameName(final Line line) {
        return name.equals(line.name);
    }

    public Long getId() {
        return id;
    }

    public String getNameValue() {
        return name.getName();
    }

    public String getColorValue() {
        return color.getColor();
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color) && Objects.equals(sections, line.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, sections);
    }
}
