import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.WeekFields;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

public class SecondJob {
    public static class StatsMapperAll
            extends Mapper<Game, NullWritable, Text, GameSummary> {

        @Override
        public void map(Game key, NullWritable value, Context context) throws IOException, InterruptedException {
            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(key.getDate(), ZoneId.systemDefault());

            int year = zonedDateTime.getYear();
            int week = zonedDateTime.get(WeekFields.ISO.weekOfWeekBasedYear());
            int month = zonedDateTime.getMonthValue();

            context.write(new Text(key.getCards().toString()),
                    new GameSummary(key.getWin() ? 1 : 0, 1, key.getClanTr(), key.getDeck() - key.getDeck2(),
                            key.getPlayer()));

            context.write(new Text(key.getCards2().toString()),
                    new GameSummary(key.getWin() ? 0 : 1, 1, key.getClanTr2(), key.getDeck2() - key.getDeck(),
                            key.getPlayer2()));

            context.write(new Text(key.getCards().toString() + "_w_" + week + "_y_" +
                    year),
                    new GameSummary(key.getWin() ? 1 : 0, 1, key.getClanTr(), key.getDeck() -
                            key.getDeck2(),
                            key.getPlayer()));

            context.write(new Text(key.getCards2().toString() + "_w_" + week + "_y_" +
                    year),
                    new GameSummary(key.getWin() ? 0 : 1, 1, key.getClanTr2(), key.getDeck2() -
                            key.getDeck(),
                            key.getPlayer2()));

            context.write(new Text(key.getCards().toString() + "_m_" + month + "_y_" +
                    year),
                    new GameSummary(key.getWin() ? 1 : 0, 1, key.getClanTr(), key.getDeck() -
                            key.getDeck2(),
                            key.getPlayer()));

            context.write(new Text(key.getCards2().toString() + "_m_" + month + "_y_" +
                    year),
                    new GameSummary(key.getWin() ? 0 : 1, 1, key.getClanTr2(), key.getDeck2() -
                            key.getDeck(),
                            key.getPlayer2()));
        }
    }

    public static class StatsCombiner
            extends Reducer<Text, GameSummary, Text, GameSummary> {
        @Override
        public void reduce(Text key, Iterable<GameSummary> values, Context context)
                throws IOException, InterruptedException {

            int wins = 0;
            int uses = 0;
            int maxClanTr = 0;
            double totalDeckDiff = 0;
            Set<String> players = new HashSet<>();
            for (GameSummary val : values) {
                if (val.getWins() == 1) {
                    wins += 1;
                    totalDeckDiff += val.getTotalDeckDiff();
                }
                uses += val.getUses();
                maxClanTr = Math.max(maxClanTr, val.getMaxClanTr());
                players.addAll(Arrays.asList(val.getPlayers()));
            }
            context.write(key, new GameSummary(wins, uses, maxClanTr, totalDeckDiff,
                    players.toString().replace("[", "").replace("]", "").replace(" ", "")));
        }
    }

    public static class StatsReducer extends Reducer<Text, GameSummary, TopK, NullWritable> {
        @Override
        public void reduce(Text key, Iterable<GameSummary> values, Context context)
                throws IOException, InterruptedException {
            int wins = 0;
            int uses = 0;
            int maxClanTr = 0;
            double totalDeckDiff = 0;
            Set<String> players = new HashSet<>();
            for (GameSummary val : values) {
                wins += val.getWins();
                uses += val.getUses();
                maxClanTr = Math.max(maxClanTr, val.getMaxClanTr());
                totalDeckDiff += val.getTotalDeckDiff();
                players.addAll(Arrays.asList(val.getPlayers().split(",")));
            }

            double avgDeckDiff = wins > 0 ? totalDeckDiff / wins : 0;
            Byte month = -1;
            Byte week = -1;
            Short year = -1;
            String stringKey = key.toString();
            String[] split = stringKey.split("_");

            if (stringKey.contains("m_")) {
                month = Byte.parseByte(split[2]);
            }
            if (stringKey.contains("w_")) {
                week = Byte.parseByte(split[2]);
            }
            if (stringKey.contains("y_")) {
                year = Short.parseShort(split[4]);
            }

            TopK topK = new TopK(stringKey.split("_")[0], month, week, year, wins, uses, maxClanTr, avgDeckDiff,
                    players.size());
            context.write(topK, NullWritable.get());
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        if (args.length == 3) {
            conf.set("NB_Reducers", args[2]);
        }
        Job job2 = Job.getInstance(conf, "SecondJob");
        job2.setNumReduceTasks(conf.getInt("NB_Reducers", 1));
        job2.setJarByClass(SecondJob.class);
        job2.setMapperClass(StatsMapperAll.class);
        job2.setCombinerClass(StatsCombiner.class);
        job2.setMapOutputKeyClass(Text.class);
        job2.setMapOutputValueClass(GameSummary.class);
        job2.setReducerClass(StatsReducer.class);
        job2.setOutputKeyClass(TopK.class);
        job2.setOutputValueClass(NullWritable.class);
        job2.setOutputFormatClass(SequenceFileOutputFormat.class);
        job2.setInputFormatClass(SequenceFileInputFormat.class);
        FileInputFormat.addInputPath(job2, new Path(args[0]));
        FileOutputFormat.setOutputPath(job2, new Path(args[1]));
        System.exit(job2.waitForCompletion(true) ? 0 : 1);
    }
}
