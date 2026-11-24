import { courseRequest } from '../request';
import type { Course, CourseAddDTO } from './types';

export function fetchCourseList() {
  return courseRequest<Course[]>({ url: '/course/list' });
}

export function fetchCourseDetail(id: number) {
  return courseRequest<Course>({ url: `/course/${id}` });
}

export function fetchAddCourse(data: CourseAddDTO) {
  return courseRequest<number>({ url: '/course', method: 'post', data });
}
