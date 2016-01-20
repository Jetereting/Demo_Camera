package com.theta360.sample.v2.model;

/**
 * ����ʹ�ó���
 * Constant used by the program
 */
public interface Constants {

    /** ������뾶 Radius of sphere for photo */
    static final int TEXTURE_SHELL_RADIUS = 2;
    /** ����������η�������Ŀ����������ż��  Number of sphere polygon partitions for photo, which must be an even number */
    static final int SHELL_DIVIDES = 40;

    /** ���ֵ����ָ��Ϊ����ӳ��ɱ�  Maximum value that can be specified as the camera FOV variable */
    static final int CAMERA_FOV_DEGREE_MAX = 100;
    /** ��Сֵ����ָ��Ϊ����ӳ��ɱ� Minimum value that can be specified as the camera FOV variable */
    static final int CAMERA_FOV_DEGREE_MIN = 30;

    /** �佹�����еļ���� Pitch width of zoom in process */
    static final float SCALE_RATIO_TICK_EXPANSION = 1.05f;
    /** �佹���̵ļ����  Pitch width of zoom out process */
    static final float SCALE_RATIO_TICK_REDUCTION = 0.95f;

    /** ��������ת��ֵ��X���� Rotation threshold for scroll (X axis direction) */
    static final double THRESHOLD_SCROLL_X = 0.02;
	/** �������᷽�򣩵���ת��ֵ Rotation threshold for scroll (Y axis direction) */
    static final double THRESHOLD_SCROLL_Y = 0.02;

	/** �������᷽�򣩵���ת���������� Rotation amount derivative parameter for scroll (X axis direction) */
    static final float ON_SCROLL_DIVIDER_X = 400.0f;
	/** �����᷽�����ת����������  Rotation amount derivative parameter for scroll (Y axis direction) */
    static final float ON_SCROLL_DIVIDER_Y = 400.0f;

    /** ��������ʱ���˶�������������X���� Movement amount derivative parameter when inertia setting is small (X axis direction) */
    static final float ON_FLING_DIVIDER_X_FOR_INERTIA_50 = 650.0f;
    /** ����������ΪС���᷽��ʱ���˶��������� ��   Movement amount derivative parameter when inertia setting is small (Y axis direction) */
    static final float ON_FLING_DIVIDER_Y_FOR_INERTIA_50 = (650.0f*3.0f);
	/**��������ʱ���˶�������������X���� Movement amount derivative parameter when inertia setting is large (X axis direction) */
    static final float ON_FLING_DIVIDER_X_FOR_INERTIA_100 = 65.0f;
	/** ����������Ϊ���᷽��ʱ���˶����������� Movement amount derivative parameter when inertia setting is large (Y axis direction) */
    static final float ON_FLING_DIVIDER_Y_FOR_INERTIA_100 = (65.0f*10.0f);

}