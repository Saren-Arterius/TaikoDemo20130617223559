LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := taikodemo-nativeaudio
LOCAL_SRC_FILES := taikodemo-nativeaudio.c
# for native audio
LOCAL_LDLIBS    += -lOpenSLES
# for native asset manager
LOCAL_LDLIBS    += -landroid

include $(BUILD_SHARED_LIBRARY)
