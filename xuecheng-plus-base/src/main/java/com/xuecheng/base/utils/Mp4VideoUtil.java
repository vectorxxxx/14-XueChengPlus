package com.xuecheng.base.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Mp4VideoUtil extends VideoUtil
{

    private String ffmpeg_path = "D:\\Program\\FFmpeg\\ffmpeg-7.0-full_build\\bin\\ffmpeg.exe";//ffmpeg的安装位置
    private String video_path = "D:\\workspace-mine\\test.avi";
    private String mp4_name = "test1.mp4";
    private String mp4folder_path = "D:\\workspace-mine\\";

    public Mp4VideoUtil(String ffmpeg_path, String video_path, String mp4_name, String mp4folder_path) {
        super(ffmpeg_path);
        this.ffmpeg_path = ffmpeg_path;
        this.video_path = video_path;
        this.mp4_name = mp4_name;
        this.mp4folder_path = mp4folder_path;
    }

    /**
     * 清除已生成的mp4
     *
     * @param mp4_path
     */
    private void clear_mp4(String mp4_path) {
        //删除原来已经生成的m3u8及ts文件
        File mp4File = new File(mp4_path);
        if (mp4File.exists() && mp4File.isFile()) {
            mp4File.delete();
        }
    }

    /**
     * 视频编码，生成mp4文件
     *
     * @return 成功返回success，失败返回控制台日志
     */
    public String generateMp4() {
        /*
        ffmpeg.exe -i  lucene.avi -c:v libx264 -s 1280x720 -pix_fmt yuv420p -b:a 63k -b:v 753k -r 18 .\lucene.mp4

        这个命令是使用FFmpeg将名为lucene.avi的视频文件转换为名为lucene.mp4的文件。下面是每个参数的解释：

        - `-i lucene.avi`: 这个参数指定输入文件的名称。在这种情况下，输入文件是lucene.avi。

        - `-c:v libx264`: 这个参数指定视频编解码器。在这里，它指定使用libx264编解码器，这是一种常用的H.264视频编解码器。
            h264：使用H.264编码器，适用于高效的视频压缩和网络传输。
            hevc：使用H.265（HEVC）编码器，提供更高的压缩效率和视频质量。
            vp9：使用VP9编码器，适用于WebM格式和在线视频播放。
            mpeg4：使用MPEG-4编码器，适用于早期的视频压缩和存储。
            prores：使用ProRes编码器，适用于高质量视频编辑和后期制作。
            libx264：使用x264编码器，提供高质量的H.264视频编码。
            libx265：使用x265编码器，提供高效的H.265视频编码。

        - `-s 1280x720`: 这个参数指定输出视频的分辨率。在这里，它将输出视频的分辨率设置为1280x720像素。
            640x480（VGA）：是较低的视频分辨率，通常用于早期的电脑显示器和视频录制设备。
            720x480（480p）：是标准视频的分辨率，通常用于DVD视频和标清电视节目。
            1280x720（720p）：也称为高清，是标准高清视频的较低分辨率，适用于网络视频、电视节目和部分电影。
            1920x1080（1080p）：也称为全高清，是高清视频的标准分辨率，通常用于电视节目、电影和在线视频。
            2560x1440（2K）：是一种高分辨率视频格式，通常用于电脑显示器和一些高端移动设备。
            3840x2160（4K）：是超高清视频的标准分辨率，提供更高的像素密度和更清晰的图像，适用于大屏幕显示和专业视频制作。
            4096x2160（DCI 4K）：是数字电影的标准分辨率，通常用于电影院放映和专业电影制作。

        - `-pix_fmt yuv420p`: 这个参数指定像素格式。在这里，它将像素格式设置为yuv420p，这是一种常见的YUV颜色编码格式。
            yuv420p：YUV420平面格式，是最常见的像素格式，适用于大多数视频编解码和播放。
            yuv422p：YUV422平面格式，色度分量的采样率介于YUV420和YUV444之间。
            yuv444p：YUV444平面格式，色度分量的采样率更高，提供更高质量的色彩表现。
            rgb24：RGB24格式，每个像素用24位来表示，适用于图像处理和合成。
            rgba：RGBA格式，带有Alpha通道，用于透明度处理和合成。
            gray：灰度格式，每个像素只有一个亮度值，适用于黑白图像处理。
            nv12：NV12格式，一种常见的YUV格式，用于视频编解码和传输。

        - `-b:a 63k`: 这个参数指定音频比特率。在这里，它将音频比特率设置为63kbps。
            音频比特率（bitrate）是指每秒钟音频数据的传输速率，通常以每秒传输的比特数（bps）来表示。音频比特率越高，音频质量通常会更好，但文件大小也会相应增加。
            128 kbps（千比特每秒）：标准的MP3音质，适合在线音乐流媒体和一般听众。
            192 kbps：更高质量的MP3音质，适合音乐爱好者和要求较高音质的场景。
            256 kbps：高质量MP3音质，适合专业音乐制作和发烧友。
            320 kbps：极高质量的MP3音质，提供最接近无损音质的体验。

        - `-b:v 753k`: 这个参数指定视频比特率。在这里，它将视频比特率设置为753kbps。
            视频比特率（bitrate）是指视频文件中每秒传输的比特数，通常以每秒传输的比特数（bps）或千比特每秒（kbps）来表示。视频比特率决定了视频文件的画质和文件大小
            720p（1280x720分辨率）：
                标清（SD）：500 kbps - 2 Mbps
                高清（HD）：2 Mbps - 6 Mbps
            1080p（1920x1080分辨率）：
                高清（HD）：4 Mbps - 10 Mbps
                全高清（Full HD）：8 Mbps - 20 Mbps
            4K（3840x2160分辨率）：
                超高清（UHD）：20 Mbps - 50 Mbps
                高动态范围（HDR）：30 Mbps - 100 Mbps

        - `-r 18`: 这个参数指定帧速率。在这里，它将帧速率设置为18帧每秒。
            帧速率（frame rate）是指视频中每秒显示的帧数，通常以每秒帧数（fps）来表示。帧速率决定了视频播放时的流畅度和动态效果
            24 fps：电影常用的帧速率，具有经典的电影感。
            30 fps：视频广播和电视节目常用的帧速率，具有良好的流畅度。
            60 fps：游戏视频和高动态范围（HDR）视频常用的帧速率，具有更高的流畅度和动态效果。

        - `.\lucene.mp4`: 这个参数指定输出文件的名称和格式。在这种情况下，输出文件是lucene.mp4。

        缩写的解释如下：
        - `-i`: input（输入）
        - `-c:v`: video codec（视频编解码器）
        - `-s`: size（尺寸）
        - `-pix_fmt`: pixel format（像素格式）
        - `-b:a`: audio bitrate（音频比特率）
        - `-b:v`: video bitrate（视频比特率）
        - `-r`: rate（帧速率）

         */
        //清除已生成的mp4
        clear_mp4(mp4folder_path + mp4_name);
        List<String> commend = new ArrayList<>();
        commend.add(ffmpeg_path);
        commend.add("-i");
        commend.add(video_path);
        commend.add("-c:v");
        commend.add("libx264");
        commend.add("-y");//覆盖输出文件
        commend.add("-s");
        commend.add("1280x720");
        commend.add("-pix_fmt");
        commend.add("yuv420p");
        commend.add("-b:a");
        commend.add("63k");
        commend.add("-b:v");
        commend.add("753k");
        commend.add("-r");
        commend.add("18");
        commend.add(mp4folder_path + mp4_name);
        String outstring = null;
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            //将标准输入流和错误输入流合并，通过标准输入流程读取信息
            builder.redirectErrorStream(true);
            Process p = builder.start();
            outstring = waitFor(p);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        Boolean check_video_time = this.check_video_time(video_path, mp4folder_path + mp4_name);
        if (!check_video_time) {
            return outstring;
        }
        else {
            return "success";
        }
    }

    public static void main(String[] args) throws IOException {
        // final ProcessBuilder builder = new ProcessBuilder();
        // builder.command("C:\\Program Files (x86)\\Tencent\\TIM\\Bin\\TIM.exe");
        // builder.redirectErrorStream(true);
        // final Process p = builder.start();

        //ffmpeg的路径
        String ffmpeg_path = "D:\\Program\\FFmpeg\\ffmpeg-7.0-full_build\\bin\\ffmpeg.exe";//ffmpeg的安装位置
        //源avi视频的路径
        String video_path = "D:\\workspace-mine\\test.avi";
        //转换后mp4文件的名称
        String mp4_name = "test.mp4";
        //转换后mp4文件的路径
        String mp4_path = "D:\\workspace-mine\\";
        //创建工具类对象
        Mp4VideoUtil videoUtil = new Mp4VideoUtil(ffmpeg_path, video_path, mp4_name, mp4_path);
        //开始视频转换，成功将返回success
        String s = videoUtil.generateMp4();
        System.out.println(s);
    }
}
