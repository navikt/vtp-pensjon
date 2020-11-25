import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'atob'
})
export class AtobPipe implements PipeTransform {

  transform(value: string | null | undefined): string | null {
    if (value != null) {
      return  decodeURIComponent(atob(value).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
      }).join(''));
    } else {
      return null
    }
  }

}
