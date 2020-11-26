import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'jsonparse'
})
export class JsonparsePipe implements PipeTransform {

  transform(value: string | null | undefined): any {
    if (value != null) {
      return JSON.parse(value)
    } else {
      return null;
    }
  }

}
