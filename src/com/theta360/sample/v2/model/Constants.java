package com.theta360.sample.v2.model;

/**
 * 程序使用常量
 * Constant used by the program
 */
public interface Constants {

    /** 光球面半径 Radius of sphere for photo */
    static final int TEXTURE_SHELL_RADIUS = 2;
    /** 光的球面多边形分区的数目，它必须是偶数  Number of sphere polygon partitions for photo, which must be an even number */
    static final int SHELL_DIVIDES = 40;

    /** 最大值可以指定为相机视场可变  Maximum value that can be specified as the camera FOV variable */
    static final int CAMERA_FOV_DEGREE_MAX = 100;
    /** 最小值可以指定为相机视场可变 Minimum value that can be specified as the camera FOV variable */
    static final int CAMERA_FOV_DEGREE_MIN = 30;

    /** 变焦过程中的间距宽度 Pitch width of zoom in process */
    static final float SCALE_RATIO_TICK_EXPANSION = 1.05f;
    /** 变焦过程的间距宽度  Pitch width of zoom out process */
    static final float SCALE_RATIO_TICK_REDUCTION = 0.95f;

    /** 滚动轴旋转阈值（X方向） Rotation threshold for scroll (X axis direction) */
    static final double THRESHOLD_SCROLL_X = 0.02;
	/** 滚动（轴方向）的旋转阈值 Rotation threshold for scroll (Y axis direction) */
    static final double THRESHOLD_SCROLL_Y = 0.02;

	/** 滚动（轴方向）的旋转量导数参数 Rotation amount derivative parameter for scroll (X axis direction) */
    static final float ON_SCROLL_DIVIDER_X = 400.0f;
	/** 滚动轴方向的旋转量导数参数  Rotation amount derivative parameter for scroll (Y axis direction) */
    static final float ON_SCROLL_DIVIDER_Y = 400.0f;

    /** 惯性设置时的运动量导数参数（X方向） Movement amount derivative parameter when inertia setting is small (X axis direction) */
    static final float ON_FLING_DIVIDER_X_FOR_INERTIA_50 = 650.0f;
    /** 当惯性设置为小（轴方向）时，运动量导数参 数   Movement amount derivative parameter when inertia setting is small (Y axis direction) */
    static final float ON_FLING_DIVIDER_Y_FOR_INERTIA_50 = (650.0f*3.0f);
	/**惯性设置时的运动量导数参数（X方向） Movement amount derivative parameter when inertia setting is large (X axis direction) */
    static final float ON_FLING_DIVIDER_X_FOR_INERTIA_100 = 65.0f;
	/** 当惯性设置为大（轴方向）时，运动量导数参数 Movement amount derivative parameter when inertia setting is large (Y axis direction) */
    static final float ON_FLING_DIVIDER_Y_FOR_INERTIA_100 = (65.0f*10.0f);

}