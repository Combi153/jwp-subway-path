package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineEntity;
import subway.dao.SectionEntity;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.LineAndStationsResponse;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationAddRequest;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public LineService(final LineRepository lineRepository, final StationRepository stationRepository, final SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public LineResponse saveLine(final LineRequest request) {
        final Line line = lineRepository.save(new LineEntity(request.getName(), request.getColor()));
        return LineResponse.of(line);
    }

    public List<LineAndStationsResponse> findLines() {
        final List<Line> lines = lineRepository.findLines();
        return lines.stream()
                .map(LineAndStationsResponse::of)
                .collect(Collectors.toUnmodifiableList());
    }

    public LineAndStationsResponse findLineById(final Long id) {
        return LineAndStationsResponse.of(lineRepository.findLineById(id));
    }

    public LineAndStationsResponse addStationToLine(final Long lineId, final StationAddRequest stationAddRequest) {
        final Line line = lineRepository.findLineById(lineId);
        final Station from = stationRepository.findStationById(stationAddRequest.getFromId());
        final Station to = stationRepository.findStationById(stationAddRequest.getToId());

        final Line insertedLine = line.insert(from, to, stationAddRequest.getDistance());

        sectionRepository.saveUpdatedSections(generateSectionEntities(line, insertedLine.getSections()), lineId);
        return LineAndStationsResponse.of(insertedLine);
    }

    private List<SectionEntity> generateSectionEntities(final Line line, final List<Section> sections) {
        return sections.stream()
                .map(section -> SectionEntity.of(section, line.getId()))
                .collect(Collectors.toUnmodifiableList());
    }

    public void deleteStationFromLine(final Long lineId, final Long stationId) {
        final Line line = lineRepository.findLineById(lineId);
        final Station station = stationRepository.findStationById(stationId);

        final Line deletedLine = line.delete(station);

        sectionRepository.saveUpdatedSections(generateSectionEntities(line, deletedLine.getSections()), lineId);
    }
}
