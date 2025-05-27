package DataAnalysis;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class WarDataAnalysis {
    static class ConflictRecord {
        int year;
        double latitude;
        double longitude;
        int casualties;
        String text;

        ConflictRecord(int year, double latitude, double longitude, int casualties, String text) {
            this.year = year;
            this.latitude = latitude;
            this.longitude = longitude;
            this.casualties = casualties;
            this.text = text;
        }
    }

    public static void main(String[] args) {
        try {
            // Create pics directory if it doesn't exist
            new File("pics").mkdirs();

            // Read CSV data
            List<ConflictRecord> records = readCsv("conflict_data.csv");

            // Perform analyses
            performTimeSeriesAnalysis(records);
            performGeospatialAnalysis(records);
            performSentimentAnalysis(records);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<ConflictRecord> readCsv(String filePath) throws IOException, CsvValidationException {
        List<ConflictRecord> records = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            reader.readNext(); // Skip header
            String[] line;
            while ((line = reader.readNext()) != null) {
                records.add(new ConflictRecord(
                        Integer.parseInt(line[0]), // year
                        Double.parseDouble(line[1]), // latitude
                        Double.parseDouble(line[2]), // longitude
                        Integer.parseInt(line[3]), // casualties
                        line[4] // text
                ));
            }
        }
        return records;
    }

    private static void performTimeSeriesAnalysis(List<ConflictRecord> records) throws IOException {
        Map<Integer, Long> conflictCountByYear = records.stream()
                .collect(Collectors.groupingBy(r -> r.year, Collectors.counting()));
        Map<Integer, Integer> casualtiesByYear = records.stream()
                .collect(Collectors.groupingBy(r -> r.year, Collectors.summingInt(r -> r.casualties)));

        TimeSeries conflictSeries = new TimeSeries("Conflict Frequency");
        conflictCountByYear.forEach((year, count) -> conflictSeries.add(new Year(year), count));

        TimeSeries casualtySeries = new TimeSeries("Total Casualties");
        casualtiesByYear.forEach((year, casualties) -> casualtySeries.add(new Year(year), casualties));

        TimeSeriesCollection dataset1 = new TimeSeriesCollection(conflictSeries);
        JFreeChart chart1 = ChartFactory.createTimeSeriesChart(
                "Conflict Frequency Over Time", "Year", "Number of Conflicts",
                dataset1, true, true, false);
        ChartUtils.saveChartAsPNG(new File("pics/conflict_frequency.png"), chart1, 800, 600);

        TimeSeriesCollection dataset2 = new TimeSeriesCollection(casualtySeries);
        JFreeChart chart2 = ChartFactory.createTimeSeriesChart(
                "Total Casualties Over Time", "Year", "Casualties",
                dataset2, true, true, false);
        ChartUtils.saveChartAsPNG(new File("pics/casualty_rates.png"), chart2, 800, 600);
    }

    private static void performGeospatialAnalysis(List<ConflictRecord> records) throws SchemaException {
        SimpleFeatureType TYPE = DataUtilities.createType("Location",
                "geometry:Point:srid=4326,name:String");
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        List<SimpleFeature> features = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            ConflictRecord record = records.get(i);
            Point point = geometryFactory.createPoint(new Coordinate(record.longitude, record.latitude));
            featureBuilder.add(point);
            featureBuilder.add("Conflict_" + i);
            features.add(featureBuilder.buildFeature(null));
        }

        MapContent map = new MapContent();
        map.setTitle("Conflict Locations");
        Style style = SLD.createPointStyle("Circle", null, null, null, 5.0f);
        Layer layer = new FeatureLayer(DataUtilities.collection(features), style);
        map.addLayer(layer);

        JMapFrame.showMap(map);
    }

    private static void performSentimentAnalysis(List<ConflictRecord> records) throws IOException {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,parse,sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        Map<String, Integer> sentimentCounts = new HashMap<>();
        sentimentCounts.put("Positive", 0);
        sentimentCounts.put("Neutral", 0);
        sentimentCounts.put("Negative", 0);

        for (ConflictRecord record : records) {
            Annotation annotation = new Annotation(record.text);
            pipeline.annotate(annotation);
            String sentiment = annotation.get(CoreAnnotations.SentencesAnnotation.class)
                    .get(0)
                    .get(SentimentCoreAnnotations.SentimentClass.class);
            sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment, 0) + 1);
        }

        try (PrintWriter writer = new PrintWriter("pics/sentiment_analysis.txt")) {
            writer.println("Sentiment Analysis Results:");
            sentimentCounts.forEach((sentiment, count) ->
                    writer.println(sentiment + ": " + count));
        }

        TimeSeries sentimentSeries = new TimeSeries("Sentiment Distribution");
        sentimentCounts.forEach((sentiment, count) -> sentimentSeries.add(new Year(count), count));
        TimeSeriesCollection dataset = new TimeSeriesCollection(sentimentSeries);
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Sentiment Distribution", "Sentiment", "Count",
                dataset, true, true, false);
        ChartUtils.saveChartAsPNG(new File("pics/sentiment_distribution.png"), chart, 800, 600);
    }
}
