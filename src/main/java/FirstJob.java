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

        public static List<Byte> hexStringToList(String hexString) {
            List<Byte> resultList = new ArrayList<>();

            // Parcours la chaîne par paires de caractères
            for (int i = 0; i < hexString.length(); i += 2) {
                String hexPair = hexString.substring(i, i + 2);
                // Convertit la paire hexadécimale en entier et l'ajoute à la liste
                Byte decimalValue = Byte.parseByte(hexPair, 16);
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

                String date = jsonNode.get("date").asText();
                Integer round = jsonNode.get("round").asInt();
                Boolean win = jsonNode.get("win").asBoolean();

                String player = jsonNode.get("player").asText();
                Double deck = jsonNode.get("deck").asDouble();
                Integer clanTr = jsonNode.get("clanTr").asInt();
                String cards = jsonNode.get("cards").asText();

                String player2 = jsonNode.get("player2").asText();
                Double deck2 = jsonNode.get("deck2").asDouble();
                Integer clanTr2 = jsonNode.get("clanTr2").asInt();
                String cards2 = jsonNode.get("cards2").asText();

                // data cleaning

                if (valideDate(date) && round != null && win != null && player != null && deck != null && clanTr != null
                        && cards != null && player2 != null && deck2 != null && clanTr2 != null && cards2 != null
                        && cards.length() >= 16 && cards2.length() >= 16) {
                    if (cards.length() > 16) {
                        cards = cards.substring(0, 16);
                    }
                    if (cards2.length() > 16) {
                        cards2 = cards2.substring(0, 16);
                    }
                    List<Byte> cardsList = hexStringToList(cards);
                    List<Byte> cardsList2 = hexStringToList(cards2);

                    Game game = new Game(Instant.parse(date), round, win, player, deck, clanTr, cardsList, player2,
                            deck2, clanTr2,
                            cardsList2);

                    context.write(game, NullWritable.get());
                }

            } catch (Exception e) {
                System.out.println("Error parsing JSON: " + e.getMessage());
            }
        }

        private boolean valideDate(String date) {
            try {
                Instant.parse(date);
                return true;
            } catch (Exception e) {
                return false;
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