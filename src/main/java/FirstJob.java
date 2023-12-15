import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FirstJob {
    public static class DataCleaningMapper
            extends Mapper<Object, Text, Game, NullWritable> {

        private final ObjectMapper mapper = new ObjectMapper();

        public static List<Integer> hexStringToList(String hexString) {
            List<Integer> resultList = new ArrayList<>();

            // Parcours la chaîne par paires de caractères
            for (int i = 0; i < hexString.length(); i += 2) {
                String hexPair = hexString.substring(i, i + 2);
                // Convertit la paire hexadécimale en entier et l'ajoute à la liste
                int decimalValue = Integer.parseInt(hexPair, 16);
                resultList.add(decimalValue);
            }
            Collections.sort(resultList);
            return resultList;
        }

        @Override
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            try {

                JsonNode jsonNode = mapper.readTree(value.toString());

                // Accéder aux champs JSON

                Instant date = Instant.parse(jsonNode.get("date").asText());
                Integer round = jsonNode.get("round").asInt();
                Integer win = jsonNode.get("win").asInt();

                String player = jsonNode.get("player").asText();
                Double deck = jsonNode.get("deck").asDouble();
                String clan = jsonNode.get("clan").asText();
                String cards = jsonNode.get("cards").asText();

                String player2 = jsonNode.get("player2").asText();
                Double deck2 = jsonNode.get("deck2").asDouble();
                String clan2 = jsonNode.get("clan2").asText();
                String cards2 = jsonNode.get("cards2").asText();

                // data cleaning

                if (date != null && round != null && win != null && player != null && deck != null && clan != null
                        && cards != null && player2 != null && deck2 != null && clan2 != null && cards2 != null
                        && cards.length() == 16 && cards2.length() == 16) {

                    List<Integer> cardsList = hexStringToList(cards);
                    List<Integer> cardsList2 = hexStringToList(cards2);

                    Game game = new Game(date, round, win, player, deck, clan, cardsList, player2, deck2, clan2,
                            cardsList2);

                    context.write(game, NullWritable.get());
                }

            } catch (Exception e) {
                System.out.println("Error parsing JSON: " + e.getMessage());
            }
        }
    }

    public static class DataCleaningReducer extends Reducer<Game, NullWritable, Game, NullWritable> {
        @Override
        public void reduce(Game key, Iterable<NullWritable> values, Context context)
                throws IOException, InterruptedException {
            context.write(key, NullWritable.get());
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "FirstJob");
        job.setNumReduceTasks(1);
        job.setJarByClass(FirstJob.class);
        job.setMapperClass(DataCleaningMapper.class);
        job.setMapOutputKeyClass(Game.class);
        job.setMapOutputValueClass(NullWritable.class);
        job.setReducerClass(DataCleaningReducer.class);
        job.setOutputKeyClass(Game.class);
        job.setOutputValueClass(NullWritable.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        job.setInputFormatClass(TextInputFormat.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

}