import java.io.IOException;
import java.util.List;

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
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

public class SecondJob {
    public static class StatsMapper
            extends Mapper<Game, NullWritable, List<Integer>, IntWritable> {

                private final IntWritable one = new IntWritable(1);
        @Override
        public void map(Game key, NullWritable value, Context context) throws IOException, InterruptedException {

            if (key.getWin() == 1) {
                context.write(key.getCards(), one);
            } else {
                context.write(key.getCards2(), one);
            }
        }
    }

    public static class StatsReducer extends Reducer<Game, IntWritable, Text, NullWritable> {
        @Override
        public void reduce(Game key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
                    int sum = 0;
                    for (IntWritable val : values) {
                        sum += val.get();
                    }
                    context.write(new Text(key.toString() + " " + sum), NullWritable.get());
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job2 = Job.getInstance(conf, "SecondJob");
        job2.setNumReduceTasks(1);
        job2.setJarByClass(SecondJob.class);
        job2.setMapperClass(StatsMapper.class);
        job2.setMapOutputKeyClass(List.class);
        job2.setMapOutputValueClass(IntWritable.class);
        job2.setReducerClass(StatsReducer.class);
        job2.setOutputKeyClass(Text.class);
        job2.setOutputValueClass(NullWritable.class);
        job2.setOutputFormatClass(SequenceFileOutputFormat.class);
        job2.setInputFormatClass(SequenceFileInputFormat.class);
        FileInputFormat.addInputPath(job2, new Path(args[0]));
        FileOutputFormat.setOutputPath(job2, new Path(args[1]));
        System.exit(job2.waitForCompletion(true) ? 0 : 1);
    }
}
