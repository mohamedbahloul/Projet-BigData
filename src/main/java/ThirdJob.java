import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

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
// import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

public class ThirdJob {
    public static class TopKMapper
            extends Mapper<TopK, NullWritable, Text, TopK> {

        @Override
        public void map(TopK key, NullWritable value, Context context) throws IOException, InterruptedException {
            if (key.getYear() != -1) {
                if (key.getMonth() != -1) {
                    context.write(new Text(key.getYear() + "_m_" + key.getMonth()), key);
                } else if (key.getWeek() != -1) {
                    context.write(new Text(key.getYear() + "_w_" + key.getWeek()), key);
                }
            } else {
                context.write(new Text("All"), key);
            }
        }
    }

    public static class TopKReducer extends Reducer<Text, TopK, Text, List<TopK>> {
        private static final int K = 50;
        private HashMap<String, TreeMap<Integer, List<TopK>>> topKsWins = new HashMap<String, TreeMap<Integer, List<TopK>>>();
        // private HashMap<String, TreeMap<Integer, String>> topKsUses = new
        // HashMap<String, TreeMap<Integer, String>>();

        @Override
        public void reduce(Text key, Iterable<TopK> values, Context context)
                throws IOException, InterruptedException {
            String date = key.toString();
            TreeMap<Integer, List<TopK>> topKWins = topKsWins.get(date);
            // TreeMap<Integer, String> topKUses = topKsUses.get(date);
            if (topKWins == null) {
                topKWins = new TreeMap<Integer, List<TopK>>();
                topKsWins.put(date, topKWins);
                // topKUses = new TreeMap<Integer, String>();
                // topKsUses.put(date, topKUses);
            }

            for (TopK value : values) {
                addToTopK(topKWins, value);
                // addToTopK(topKUses, value.getCards(), value.getUses());
            }

            // context.write(new Text(date), NullWritable.get());
            for (Integer wins : topKWins.descendingKeySet()) {
                context.write(new Text(date), topKWins.get(wins));
            }
            // context.write(new Text(""), NullWritable.get());
            // for (Integer uses : topKUses.descendingKeySet()) {
            // context.write(new Text("\tUses: " + uses + " Decks: " + topKUses.get(uses)),
            // NullWritable.get());
            // }
            // context.write(new Text(""), NullWritable.get());
            // context.write(new Text(""), NullWritable.get());
            // context.write(new Text(""), NullWritable.get());
        }

        private void addToTopK(TreeMap<Integer, List<TopK>> topK, TopK deck) {
            Integer wins = deck.getWins();
        
            if (topK.containsKey(wins)) {
                List<TopK> old_decks = topK.get(wins);
                List<TopK> new_decks = new ArrayList<>(old_decks);
                new_decks.add(new TopK(deck));
                topK.put(wins, new_decks);
            } else {
                List<TopK> decks = new ArrayList<>();
                decks.add(new TopK(deck));
                if (topK.size() < K) {
                    topK.put(wins, decks);
                } else {
                    Integer first = topK.firstKey();
                    if (wins.intValue() > first.intValue()) {
                        topK.remove(first);
                        topK.put(wins, decks);
                    }
                }
            }
        }            
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job3 = Job.getInstance(conf, "ThirdJob");
        job3.setNumReduceTasks(1);
        job3.setJarByClass(ThirdJob.class);
        job3.setMapperClass(TopKMapper.class);
        job3.setMapOutputKeyClass(Text.class);
        job3.setMapOutputValueClass(TopK.class);
        job3.setReducerClass(TopKReducer.class);
        job3.setOutputKeyClass(Text.class);
        job3.setOutputValueClass(List.class);
        job3.setOutputFormatClass(SequenceFileOutputFormat.class);
        job3.setInputFormatClass(SequenceFileInputFormat.class);
        FileInputFormat.addInputPath(job3, new Path(args[0]));
        FileOutputFormat.setOutputPath(job3, new Path(args[1]));
        System.exit(job3.waitForCompletion(true) ? 0 : 1);
    }
}