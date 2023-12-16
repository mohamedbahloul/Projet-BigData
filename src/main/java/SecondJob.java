import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class SecondJob {
    public static class StatsMapper
            extends Mapper<Game, NullWritable, Text, GameSummary> {

        @Override
        public void map(Game key, NullWritable value, Context context) throws IOException, InterruptedException {
            context.write(new Text(key.getCards().toString()),
                    new GameSummary(key.getWin(), 1, key.getClanTr(), key.getDeck() - key.getDeck2(), key.getPlayer()));
            context.write(new Text(key.getCards2().toString()), new GameSummary(!key.getWin(), 1, key.getClanTr2(),
                    key.getDeck2() - key.getDeck(), key.getPlayer2()));
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
                if (val.getWins()) {
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

    public static class StatsReducer extends Reducer<Text, GameSummary, Text, NullWritable> {
        @Override
        public void reduce(Text key, Iterable<GameSummary> values, Context context)
                throws IOException, InterruptedException {
            int wins = 0;
            int uses = 0;
            int maxClanTr = 0;
            double totalDeckDiff = 0;
            Set<String> players = new HashSet<>();
            for (GameSummary val : values) {
                wins += val.getWinsPartialCount();
                uses += val.getUses();
                maxClanTr = Math.max(maxClanTr, val.getMaxClanTr());
                totalDeckDiff += val.getTotalDeckDiff();
                players.addAll(Arrays.asList(val.getPlayers().split(",")));
            }

            double avgDeckDiff = wins > 0 ? totalDeckDiff / wins : 0;
            context.write(new Text(key.toString() + " " + wins + " " + uses + " " + maxClanTr + " " + avgDeckDiff + " "
                    + players.size()), NullWritable.get());
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job2 = Job.getInstance(conf, "SecondJob");
        job2.setNumReduceTasks(1);
        job2.setJarByClass(SecondJob.class);
        job2.setMapperClass(StatsMapper.class);
        job2.setCombinerClass(StatsCombiner.class);
        job2.setMapOutputKeyClass(Text.class);
        job2.setMapOutputValueClass(GameSummary.class);
        job2.setReducerClass(StatsReducer.class);
        job2.setOutputKeyClass(Text.class);
        job2.setOutputValueClass(NullWritable.class);
        job2.setOutputFormatClass(TextOutputFormat.class);
        job2.setInputFormatClass(SequenceFileInputFormat.class);
        FileInputFormat.addInputPath(job2, new Path(args[0]));
        FileOutputFormat.setOutputPath(job2, new Path(args[1]));
        System.exit(job2.waitForCompletion(true) ? 0 : 1);
    }
}
